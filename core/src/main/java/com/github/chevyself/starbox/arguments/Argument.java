package com.github.chevyself.starbox.arguments;

import com.github.chevyself.starbox.annotations.Free;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.exceptions.CommandRegistrationException;
import com.github.chevyself.starbox.exceptions.MissingArgumentException;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import com.github.chevyself.starbox.util.Pair;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.NonNull;

/**
 * This class represents an argument of a command. An argument can change the output of a command,
 * this type is used for commands that are parsed using reflection.
 *
 * <p>There's two types of arguments:
 *
 * <ul>
 *   <li>{@link ExtraArgument} which is not exactly given by the user but by the context of the
 *       command execution, that is why it does not require an annotation as you can see in {@link
 *       #isEmpty(Annotation[])} if this method returns true it will be considered as an {@link
 *       ExtraArgument}
 *   <li>{@link SingleArgument} this argument expects a user input unless it is annotated with
 *       {@link Free} or {@link Required}. It has a place inside the command usage:
 *       [prefix][command] [argument]...
 *       <p>This kind of argument has two different {@link ArgumentBehaviour}
 * </ul>
 *
 * <p>To know how arguments are parsed you can check {@link #parseArguments(Method)} or {@link
 * #parseArguments(Class[], Annotation[][])} and to know how a single argument is parsed see {@link
 * #parseArgument(Class, Annotation[], int)}
 *
 * <p>Here's an example:
 *
 * <pre>{@code
 *  public class ArgumentsSample {
 *
 *     public static void main(String[] args) throws NoSuchMethodException {
 *         // Parsing from the AMethod of this same class
 *         List<Argument<?>> arguments = Argument.parseArguments(ArgumentsSample.class.getMethod("AMethod", StarboxCommandContext.class, String.class, String.class, String[].class));
 *         for (Argument<?> argument : arguments) {
 *             System.out.println("argument = " + argument);
 *         }
 *         // Output:
 *         // argument = ExtraArgument{clazz=interface me.googas.commands.context.StarboxCommandContext}
 *         // argument = SingleArgument{name='No name provided', description='No description provided', suggestions=[], clazz=class java.lang.String, required=true, position=0}
 *         // argument = SingleArgument{name='No name provided', description='No description provided', suggestions=[], clazz=class java.lang.String, required=false, position=1}
 *         // argument = MultipleArgument{minSize=1, maxSize=-1} SingleArgument{name='No name provided', description='No description provided', suggestions=[], clazz=class [Ljava.lang.String;, required=true, position=2}
 *     }
 *
 *     public void AMethod(StarboxCommandContext context, @Required String name, @Free String description, @Required @Multiple String[] messages) {
 *         // Has 4 arguments
 *         // An ExtraArgument: the context
 *         // Two SingleArgument: The name and description
 *         // A MultipleArgument: the messages
 *     }
 * }
 * }</pre>
 *
 * <p>To know how to create usage messages check: {@link #generateUsage(List)}
 *
 * @param <O> the type of the class that the argument has to supply
 */
public interface Argument<O> {

  /**
   * Parse the arguments from a {@link Method}. This will use {@link #parseArguments(Class[],
   * Annotation[][])} using {@link Method#getParameterTypes()} and {@link
   * Method#getParameterAnnotations()}
   *
   * @param method the method to parse the arguments from
   * @return the list of arguments from the method
   */
  @NonNull
  static List<Argument<?>> parseArguments(@NonNull Method method) {
    return Argument.parseArguments(method.getParameterTypes(), method.getParameterAnnotations());
  }

  /**
   * Parse the list of arguments from an array of parameters and its annotations.
   *
   * <p>To parse each argument this will iterate through the parameter class and its annotations
   * checking that {@link #isEmpty(Annotation[])} if this method results true it will return an
   * {@link ExtraArgument} else it will use the method {@link #parseArgument(Class, Annotation[],
   * int)} and the position will increase for each {@link SingleArgument}.
   *
   * @param parameters the array of parameters
   * @param annotations the array of annotations for each parameter
   * @return the list of parsed items of this class
   */
  @NonNull
  static List<Argument<?>> parseArguments(
      @NonNull final Class<?>[] parameters, @NonNull final Annotation[][] annotations) {
    List<Argument<?>> arguments = new ArrayList<>();
    int position = 0;
    for (int i = 0; i < parameters.length; i++) {
      Annotation[] paramAnnotations = annotations[i];
      if (Argument.isEmpty(paramAnnotations)) {
        arguments.add(i, new ExtraArgument<>(parameters[i]));
      } else {
        SingleArgument<?> argument =
            Argument.parseArgument(parameters[i], annotations[i], position);
        arguments.add(i, argument);
        position++;
      }
    }
    return arguments;
  }

  /**
   * Parse the argument using the parameter class and the annotations of the parameter.
   *
   * <p>This requires that the argument has any of the two annotations: {@link Required} or {@link
   * Free} if that is not the case then an exception will be thrown.
   *
   * <p>A simple iteration will be done in order to get any of the two annotations
   *
   * @param parameter the class of the parameter
   * @param annotations the annotations of the parameter
   * @param position the position of the parameter given by the {@link #parseArguments(Class[],
   *     Annotation[][])}
   * @return the final parsed argument as a {@link SingleArgument}
   * @throws CommandRegistrationException if the parameter does not contain an annotation such as
   *     {@link Required} or {@link Free}
   */
  @NonNull
  static SingleArgument<?> parseArgument(
      @NonNull Class<?> parameter, @NonNull Annotation[] annotations, int position) {
    for (Annotation annotation : annotations) {
      if (annotation instanceof Required) {
        Required required = (Required) annotation;
        String name = required.name();
        String description = required.description();
        List<String> suggestions = Arrays.asList(required.suggestions());
        return Argument.getArgument(
            parameter, position, true, name, description, suggestions, required.behaviour());
      } else if (annotation instanceof Free) {
        Free free = (Free) annotation;
        String name = free.name();
        String description = free.description();
        List<String> suggestions = Arrays.asList(free.suggestions());
        return Argument.getArgument(
            parameter, position, false, name, description, suggestions, free.behaviour());
      }
    }
    throw new CommandRegistrationException(
        "SingleArgument could not be parsed for "
            + parameter
            + " because it may not contain the annotations "
            + Required.class
            + " or "
            + Free.class);
  }

  /**
   * Checks if the annotations array does not contain either the {@link Required} or {@link Free}
   * annotations. Loops the array and checks if it has either one.
   *
   * @param annotations the array of annotations to check
   * @return true if the array does not contain either of both annotations
   */
  static boolean isEmpty(@NonNull Annotation[] annotations) {
    for (Annotation annotation : annotations) {
      if (annotation instanceof Required || annotation instanceof Free) {
        return false;
      }
    }
    return true;
  }

  /**
   * This gets the final instance of the argument. Called by {@link #parseArgument(Class,
   * Annotation[], int)}
   *
   * @param parameter the class of the parameter
   * @param position the position of the argument
   * @param required whether the argument is required (if it has the annotation {@link Required} it
   *     is required)
   * @param name the name of the argument
   * @param description the description of the argument
   * @param suggestions the suggestions for the argument
   * @param behaviour the behaviour of the argument
   * @return the argument instance
   */
  @NonNull
  static SingleArgument<?> getArgument(
      @NonNull Class<?> parameter,
      int position,
      boolean required,
      @NonNull String name,
      @NonNull String description,
      @NonNull List<String> suggestions,
      @NonNull ArgumentBehaviour behaviour) {
    return new SingleArgument<>(
        name, description, suggestions, behaviour, parameter, required, position);
  }

  /**
   * Generates a usage message for the provided item of this class
   *
   * <p>A list of arguments may also be used to get the usage of a command. From <a
   * href="https://en.wikipedia.org/wiki/Usage_message">Wikipedia</a>
   *
   * <ul>
   *   <li>Required arguments are indicated with angles brackets: '&lt;&gt;' ex: '&lt;name&gt;'
   *   <li>Free arguments are indicated with square brackets: '[]' ex: '[name]'
   *   <li>Flags start with the vertical bar and are indicated using square brackets: ex: [-f]
   *   <li>Flags with a value are just like normal flags but the value is separated with a single
   *       space ' ' ex: [-f &lt;value&gt;] They can also contain a key and a value as such: [-f
   *       &lt;key&gt;=&lt;value&gt;] with the equals sign '=' separating the key and value
   * </ul>
   *
   * <p>Here's an example:
   *
   * <p>Usage: command [-f] [--help | -H] [-c &lt;child_name&gt;] [-p &lt;number&gt;] &lt;arg1&gt;
   * &lt;arg2&gt; [arg3]
   *
   * @param arguments the list of arguments used to generate the usage message
   * @return the usage message which may be empty if the {@link List} is empty or there's no {@link
   *     SingleArgument}
   */
  @NonNull
  static String generateUsage(@NonNull List<Argument<?>> arguments) {
    StringBuilder builder = new StringBuilder();
    for (Argument<?> argument : arguments) {
      if (!(argument instanceof SingleArgument)) {
        continue;
      }
      String name = ((SingleArgument<?>) argument).getName();
      if (((SingleArgument<?>) argument).isRequired()) {
        builder.append("<").append(name).append("> ");
      } else {
        builder.append("[").append(name).append("] ");
      }
    }
    return builder.toString();
  }

  /**
   * Parses from a {@link String}. It must be formatted as follows:
   *
   * <h1>IMPORTANT</h1>
   *
   * <p>The parameter mappings are a key {@link String} which represents name for a {@link Class}
   * this means that:
   *
   * <ul>
   *   <li>String = java.lang.String
   *   <li>Long = java.lang.Long
   * </ul>
   *
   * <p>Are possible mappings which may be used in the parse of a command
   *
   * <p>If the {@link String} starts with a '@' it will be considered as a {@link
   * ArgumentBehaviour#CONTINUOUS} annotation next (Ignoring the optional starting '@'): The {@link
   * String} must start with '&lt;' and end with '&gt;' for a required argument and start with '['
   * and end with ']' for an optional argument.
   *
   * <p>The {@link Class}, name and description must be split by a ':' and replace the spaces of the
   * description with '-'
   *
   * <p>Some correct formatted strings, using the mappings shown above would be as follows:
   *
   * <ul>
   *   <li>@&lt;String:name:The-name-of-a-user&gt;
   *   <li>&lt;Long:number&gt;
   *   <li>[JoinedStrings:strings]
   * </ul>
   *
   * @param mappings the map of {@link Class} mappings
   * @param string the string to parse
   * @param suggestions the suggestions that can be given to input to check {@link
   *     SingleArgument#getSuggestions(StarboxCommandContext)}
   * @param position the position in which the argument must be input
   * @return the parsed argument
   * @throws IllegalArgumentException if the {@link String} does not start and end with either
   *     '&lt;&gt;' or '[]', or if the {@link String} does not contain ':' separating the {@link
   *     Class} and name (description is optional) and if the key for the {@link Class} mapping does
   *     not map to anything (it is case-sensitive)
   */
  @NonNull
  static Argument<?> parse(
      @NonNull Map<String, String> mappings,
      @NonNull String string,
      @NonNull List<String> suggestions,
      int position) {
    return Argument.parse(mappings, string, suggestions, ArgumentBehaviour.NORMAL, position);
  }

  /**
   * Recursive method for {@link #parse(Map, String, List, int)}.
   *
   * @param mappings the map of {@link Class} mappings
   * @param string the string to parse
   * @param suggestions the suggestions that can be given to input the argument check {@link
   *     SingleArgument#getSuggestions(StarboxCommandContext)}
   * @param behaviour the behaviour of the argument
   * @param position the position in which the argument must be input
   * @return the parsed argument
   * @throws IllegalArgumentException if the {@link String} does not start and end with either
   *     '&lt;&gt;' or '[]', or if the {@link String} does not contain ':' separating the {@link
   *     Class} and name (description is optional) and if the key for the {@link Class} mapping does
   *     not map to anything (it is case-sensitive)
   */
  @NonNull
  static Argument<?> parse(
      @NonNull Map<String, String> mappings,
      @NonNull String string,
      @NonNull List<String> suggestions,
      @NonNull ArgumentBehaviour behaviour,
      int position) {
    if (string.startsWith("@")) {
      return Argument.parse(
          mappings, string.substring(1), suggestions, ArgumentBehaviour.CONTINUOUS, position);
    } else {
      boolean required;
      if (string.startsWith("<") && string.endsWith(">")) {
        required = true;
      } else if (string.startsWith("[") && string.endsWith("]")) {
        required = false;
      } else {
        throw new IllegalArgumentException(
            string + " does not start and end with either: '<>' or '[]'");
      }
      string = string.substring(1, string.length() - 1);
      String[] split = string.split(":");
      if (split.length < 2) {
        throw new IllegalArgumentException(
            string + " does not have ':' separating class, name and description");
      }
      try {
        String key = split[0];
        String value = mappings.get(key);
        if (value == null) {
          throw new IllegalArgumentException(key + " did not match any mapping");
        }
        Class<?> clazz = Class.forName(mappings.get(split[0]));
        String name = split[1];
        String description = split.length > 2 ? split[2].replace("-", " ") : "No description given";
        return new SingleArgument<>(
            name, description, suggestions, behaviour, clazz, required, position);
      } catch (ClassNotFoundException e) {
        throw new IllegalArgumentException(
            string + " does not have a correct mapping in: " + split[0]);
      }
    }
  }

  /**
   * Parse from a single {@link String}. Many arguments using {@link #parse(Map, String, List, int)}
   * the {@link String} will be split using the character ' '
   *
   * @param mappings the map of {@link Class} mappings
   * @param string the string to parse
   * @return the parsed arguments
   */
  @NonNull
  static List<Argument<?>> parseArguments(
      @NonNull Map<String, String> mappings, @NonNull String string) {
    String[] split = string.split(" ");
    List<Argument<?>> arguments = new ArrayList<>(split.length);
    for (int i = 0; i < split.length; i++) {
      arguments.add(Argument.parse(mappings, split[i], new ArrayList<>(), i));
    }
    return arguments;
  }

  /**
   * Get the class of the argument.
   *
   * <pre>
   * public void AMethod(StarboxCommandContext context) {
   *       // The class of the argument is StarboxCommandContext
   * }
   * </pre>
   *
   * @return the class of the argument
   */
  @NonNull
  Class<O> getClazz();

  /**
   * Process the object that must be returned by this argument in the command execution.
   *
   * @param <T> the type of the context that is processing the argument
   * @param registry the registry to get the object of the argument
   * @param messages the messages for the exceptions in case anything goes wrong
   * @param context the context where the argument must be processed
   * @param lastIndex the last index of the command execution
   * @return the processed object queried from this argument
   * @throws ArgumentProviderException if the object of the argument cannot be provided
   * @throws MissingArgumentException if the argument is required and there's no input
   */
  <T extends StarboxCommandContext<T, ?>> Pair<Object, Integer> process(
      @NonNull ProvidersRegistry<T> registry,
      @NonNull MessagesProvider<T> messages,
      @NonNull T context,
      int lastIndex)
      throws ArgumentProviderException, MissingArgumentException;
}
