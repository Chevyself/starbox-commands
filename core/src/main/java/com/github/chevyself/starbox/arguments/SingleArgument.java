package com.github.chevyself.starbox.arguments;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.exceptions.MissingArgumentException;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.providers.StarboxArgumentProvider;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import com.github.chevyself.starbox.util.Pair;
import com.github.chevyself.starbox.util.objects.Mappable;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;

/**
 * This argument just like a {@link SingleArgument} but it has many places in a command which means
 * that multiple inputs are allowed.
 *
 * <p>[prefix][command] [argument 1] [argument 1] [argument 1] [argument 2]
 *
 * <p>[prefix][command] [argument 1] [argument 2] [argument 2] [argument 2]
 *
 * <p>The best example is the name or the age for a user that is being created
 *
 * @param <O> the type of the class that the argument has to supply
 */
public class SingleArgument<O> implements Argument<O>, Mappable {

  @NonNull @Getter private final String name;
  @NonNull @Getter private final String description;
  @NonNull private final List<String> suggestions;
  @NonNull @Getter private final ArgumentBehaviour behaviour;
  @NonNull @Getter private final Class<O> clazz;
  @Getter private final boolean required;
  @Getter private final int position;

  /**
   * Get a new instance of a single argument.
   *
   * @param name the name of the argument
   * @param description the description of the argument
   * @param suggestions the suggestions of the argument
   * @param behaviour the behaviour of the argument
   * @param clazz the class of the argument
   * @param required is the argument required by the command
   * @param position the position of the argument in the command
   */
  public SingleArgument(
      @NonNull String name,
      @NonNull String description,
      @NonNull List<String> suggestions,
      @NonNull ArgumentBehaviour behaviour,
      @NonNull Class<O> clazz,
      boolean required,
      int position) {
    this.name = name;
    this.description = description;
    this.suggestions = suggestions;
    this.clazz = clazz;
    this.behaviour = behaviour;
    this.required = required;
    this.position = position;
  }

  /**
   * Get a list of suggestions to successfully use the argument.
   *
   * @param context the command context to help use the suggestions
   * @return the list of suggestions
   */
  @NonNull
  public List<String> getSuggestions(@NonNull StarboxCommandContext<?, ?> context) {
    return this.suggestions;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", SingleArgument.class.getSimpleName() + "[", "]")
        .add("name='" + name + "'")
        .add("description='" + description + "'")
        .add("suggestions=" + suggestions)
        .add("clazz=" + clazz)
        .add("required=" + required)
        .add("position=" + position)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    SingleArgument<?> that = (SingleArgument<?>) o;
    return required == that.required
        && position == that.position
        && name.equals(that.name)
        && description.equals(that.description)
        && suggestions.equals(that.suggestions)
        && clazz.equals(that.clazz);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, suggestions, clazz, required, position);
  }

  @Override
  public <T extends StarboxCommandContext<T, ?>> Pair<Object, Integer> process(
      @NonNull ProvidersRegistry<T> registry,
      @NonNull MessagesProvider<T> messages,
      @NonNull T context,
      int lastIndex)
      throws ArgumentProviderException, MissingArgumentException {
    Object object;
    Pair<String, Integer> argumentString = SingleArgument.getArgument(this, context, lastIndex);
    String string = argumentString.getA();
    if (string == null) {
      if (this.isRequired()) {
        throw new MissingArgumentException(
            messages.missingArgument(
                this.getName(), this.getDescription(), this.getPosition(), context));
      } else {
        object = null;
      }
    } else {
      object = registry.fromString(string, this.getClazz(), context);
    }
    return new Pair<>(object, argumentString.getB());
  }

  /**
   * Get the string that will be used to get the object to pass to the command method as a parameter
   * (Check {@link StarboxArgumentProvider}).
   *
   * @param argument the argument that requires the object
   * @param context the context of the command execution
   * @param lastIndex where do arguments originate
   * @return the obtained string and the amount to increase the last index
   */
  @NonNull
  static Pair<String, Integer> getArgument(
      @NonNull SingleArgument<?> argument,
      @NonNull StarboxCommandContext<?, ?> context,
      int lastIndex) {
    List<String> arguments = context.getCommandLineParser().getArguments();
    String string = null;
    int increase = 0;
    if (arguments.size() - 1 < argument.getPosition() + lastIndex) {
      if (!argument.isRequired() & !argument.getSuggestions(context).isEmpty()) {
        string = argument.getSuggestions(context).get(0);
      }
    } else {
      if (argument.getBehaviour().equals(ArgumentBehaviour.CONTINUOUS)) {
        string =
            String.join(
                " ", arguments.subList(argument.getPosition() + lastIndex, arguments.size()));
      } else {
        Pair<String, Integer> pair =
            SingleArgument.getNormalArgument(arguments, argument.getPosition() + lastIndex);
        string = pair.getA();
        increase = pair.getB();
      }
    }
    return new Pair<>(string, increase);
  }

  /**
   * Parses the correct normal argument from the list of arguments.
   *
   * @param arguments the list of arguments
   * @param position the starting position of the argument
   * @return the parsed string and the increase in the index
   */
  @NonNull
  static Pair<String, Integer> getNormalArgument(@NonNull List<String> arguments, int position) {
    int increase = 0;
    String initial = arguments.get(position);
    if (!initial.startsWith("\"")) {
      return new Pair<>(initial, 0);
    }
    StringBuilder builder = new StringBuilder();
    for (int i = position; i < arguments.size(); i++) {
      builder.append(arguments.get(i)).append(" ");
      if (arguments.get(i).endsWith("\"") && !arguments.get(i).endsWith("\\\"")) {
        increase = i - position;
        break;
      }
    }
    builder.deleteCharAt(builder.length() - 1);
    String string = builder.substring(1, builder.length() - 1).replace("\\\"", "\"");
    return new Pair<>(string, increase);
  }
}
