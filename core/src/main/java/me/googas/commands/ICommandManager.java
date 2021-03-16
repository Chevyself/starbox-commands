package me.googas.commands;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import lombok.NonNull;
import me.googas.commands.annotations.Multiple;
import me.googas.commands.annotations.Optional;
import me.googas.commands.annotations.Parent;
import me.googas.commands.annotations.Required;
import me.googas.commands.annotations.Settings;
import me.googas.commands.arguments.Argument;
import me.googas.commands.arguments.ExtraArgument;
import me.googas.commands.arguments.ISimpleArgument;
import me.googas.commands.arguments.MultipleArgument;
import me.googas.commands.exceptions.CommandRegistrationException;
import me.googas.commands.objects.CommandSettings;

/**
 * The core or "heart" interface of the framework. It is used to register the commands using
 * reflection getting the name, description, arguments, etc. From the methods of the class.
 *
 * @param <C> the type of simple command that this manager registers
 */
public interface ICommandManager<C extends ISimpleCommand<?>> {

  /**
   * Registers the command inside the manager. This has to be different for each framework as it can
   * have some incompatibilities between each other. For example: JDA uses a different permission
   * system than Bukkit or Bungee
   *
   * @param object the class instance of the command. It will be used to invoke the methods and also
   *     to get the class and therefore the methods to invoke for a command
   */
  void registerCommand(@NonNull Object object);

  /**
   * Parse the arguments of a command. This method looks for the parameters of the method using
   * reflection. If a parameter is not annotated with anything (check {@link
   * #isEmpty(Annotation[])}) it will be considered as a {@link ExtraArgument} else it is a {@link
   * Argument}. When an argument is annotated with {@link Multiple} it means that the argument
   * requires multiple strings, the strings will be taken from the position of the argument. With
   * each argument a position will increase except if the argument is {@link ExtraArgument}
   *
   * @param parameters the parameters of the command method
   * @param annotations the annotations of the parameters of the command method
   * @return the list of parsed {@link ISimpleArgument} empty if there's none
   */
  @NonNull
  default List<ISimpleArgument<?>> parseArguments(
      @NonNull final Class<?>[] parameters, @NonNull final Annotation[][] annotations) {
    List<ISimpleArgument<?>> arguments = new ArrayList<>();
    int position = 0;
    for (int i = 0; i < parameters.length; i++) {
      Annotation[] paramAnnotations = annotations[i];
      if (this.isEmpty(paramAnnotations)) {
        arguments.add(i, new ExtraArgument<>(parameters[i]));
      } else {
        Argument<?> argument = this.parseArgument(parameters[i], annotations[i], position);
        arguments.add(i, argument);
        if (argument instanceof MultipleArgument) {
          position = +((MultipleArgument<?>) argument).getMinSize();
        } else {
          position++;
        }
      }
    }
    return arguments;
  }

  /**
   * Parse all the commands from the given object. Please note that this commands will not be
   * registered, to do so please
   *
   * @see #registerCommand(Object)
   * @param object the object to get the commands
   * @return a collection with the parsed commands
   */
  @NonNull
  Collection<C> parseCommands(@NonNull Object object);

  /**
   * Get a parent command using its alias
   *
   * @param alias the alias of the parent
   * @return the prent command if it is registered else null
   */
  IParentCommand<C> getParent(@NonNull String alias);

  /** Unregister this command manager and all of its commands */
  void unregister();

  /**
   * Get all the commands that have been registered in this manager
   *
   * @return a collection of commands
   */
  @NonNull
  Collection<C> getCommands();

  /**
   * This should be executed from {@link #registerCommand(Object)} to get a command from a method
   * and register it. This method is the one that will get stuff as the name, description and
   * permission of the command as well as calling {@link #parseArguments(Class[], Annotation[][])}
   * to get the arguments
   *
   * @param object the class instance of the command. This is the same object as send in {@link
   *     #registerCommand(Object)}
   * @param method the method to run the command this is the method annotated with the
   *     respective @Command annotation.
   * @param isParent is the command a parent. This is true if the method contains the annotation
   *     {@link Parent}
   * @return the new instance of {@link ICommand} if it was parsed correctly, method is accessible,
   *     etc, etc.
   */
  @NonNull
  C parseCommand(@NonNull Object object, @NonNull Method method, boolean isParent);

  /**
   * Parse the argument using the parameter class and the annotation. It is called by <br>
   * {@link #parseArguments(Class[], Annotation[][])} to get each single instance of argument. If
   * the parameter does not contain an annotation this should not be called use <br>
   * {@link ExtraArgument} instead
   *
   * @param parameter the parameter of the method of the command
   * @param annotations the annotations of the parameter used to determinate if it is a required
   *     argument or not. Check {@link Required} and {@link Optional}
   * @param position the position of the parameter given by the {@link #parseArguments(Class[],
   *     Annotation[][])}
   * @return the final parsed argument
   * @throws CommandRegistrationException if the parameter does not contain an annotation such as
   *     {@link Required} or {@link Optional}
   */
  @NonNull
  default Argument<?> parseArgument(
      @NonNull Class<?> parameter, @NonNull Annotation[] annotations, int position) {
    Multiple multiple = this.getMultiple(annotations);
    for (Annotation annotation : annotations) {
      if (annotation instanceof Required) {
        String name = ((Required) annotation).name();
        String description = ((Required) annotation).description();
        List<String> suggestions = Arrays.asList(((Required) annotation).suggestions());
        return getArgument(parameter, position, multiple, true, name, description, suggestions);
      } else if (annotation instanceof Optional) {
        String name = ((Optional) annotation).name();
        String description = ((Optional) annotation).description();
        List<String> suggestions = Arrays.asList(((Optional) annotation).suggestions());
        return getArgument(parameter, position, multiple, false, name, description, suggestions);
      }
    }
    throw new CommandRegistrationException(
        "Argument could not be parsed for "
            + parameter
            + " because it may not contain the annotations "
            + Required.class
            + " or "
            + Optional.class);
  }

  /**
   * Get the annotation {@link Multiple} from an array of annotations
   *
   * @param annotations the array of annotations
   * @return the annotation if the array contains it else null
   */
  default Multiple getMultiple(@NonNull Annotation[] annotations) {
    for (Annotation annotation : annotations) {
      if (annotation instanceof Multiple) {
        return (Multiple) annotation;
      }
    }
    return null;
  }

  /**
   * This gets the final instance of the argument. Called by {@link #parseArgument(Class,
   * Annotation[], int)} basically this gives either {@link MultipleArgument} or {@link Argument} it
   * is check in the {@link #parseArgument(Class, Annotation[], int)} method if the parameter
   * contains the annotation {@link Multiple}
   *
   * @param parameter the parameter where the argument came from
   * @param position the position of the argument
   * @param multiple the annotation required to get an {@link MultipleArgument} it can be null
   * @param required whether the argument is required (if it has the annotation {@link Required} it
   *     is required)
   * @param name the name of the argument
   * @param description the description of the argument
   * @param suggestions the suggestions for the argument
   * @return the argument instance if multiple is tru it will be a {@link MultipleArgument} else
   *     just a {@link Argument}
   */
  @NonNull
  default Argument<?> getArgument(
      @NonNull Class<?> parameter,
      int position,
      Multiple multiple,
      boolean required,
      @NonNull String name,
      @NonNull String description,
      @NonNull List<String> suggestions) {
    if (multiple != null) {
      return new MultipleArgument<>(
          name,
          description,
          suggestions,
          parameter,
          true,
          position,
          multiple.min(),
          multiple.max());
    } else {
      return new Argument<>(name, description, suggestions, parameter, required, position);
    }
  }

  /**
   * Checks if an array of annotations has certain annotation. This method loops around the array,
   * if an annotation from the array matches the class to search it will return true
   *
   * @param annotations the array of annotations
   * @param search the annotation to match
   * @return true if the array has the annotation else if the loop ends without matching return
   *     false
   * @param <T> the type of annotation to match
   */
  default <T extends Annotation> boolean hasAnnotation(
      @NonNull Annotation[] annotations, @NonNull Class<T> search) {
    for (Annotation annotation : annotations) {
      if (search.isAssignableFrom(annotation.getClass())) return true;
    }
    return false;
  }

  /**
   * Checks if the annotations array does not contain either the {@link Required} or {@link
   * Optional} annotations. Loops around the array and check if it has either of too.
   *
   * @param annotations the array of annotations to check
   * @return true if the method does not contain either of both annotations
   */
  default boolean isEmpty(@NonNull Annotation[] annotations) {
    for (Annotation annotation : annotations) {
      if (annotation instanceof Required || annotation instanceof Optional) return false;
    }
    return true;
  }

  /**
   * Gets the settings for a command. This will check the method of the command and the annotation
   * {@link Settings}
   *
   * @param method the method of a command to get the annotations
   * @return the settings as a {@link HashMap}
   */
  @NonNull
  default CommandSettings parseSettings(@NonNull Method method) {
    if (method.isAnnotationPresent(Settings.class)) {
      return CommandSettings.constructCommand(method.getAnnotation(Settings.class).value());
    }
    return new CommandSettings();
  }
}
