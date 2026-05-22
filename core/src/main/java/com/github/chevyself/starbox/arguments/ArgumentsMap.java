package com.github.chevyself.starbox.arguments;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import lombok.NonNull;

/**
 * Helper object to pass in {@link
 * com.github.chevyself.starbox.commands.CommandBuilder#executes(BiFunction)} to get the arguments
 * and extra arguments.
 */
public class ArgumentsMap {

  @NonNull private final Map<String, Object> arguments;
  @NonNull private final List<Object> extra;

  /**
   * Create a new arguments map.
   *
   * @param arguments the arguments
   * @param extra the extra arguments
   */
  public ArgumentsMap(@NonNull Map<String, Object> arguments, @NonNull List<Object> extra) {
    this.arguments = arguments;
    this.extra = extra;
  }

  /**
   * Get an argument by name.
   *
   * @param name the name of the argument
   * @return the argument
   * @throws IllegalArgumentException if no argument with the name exists
   */
  public Object get(@NonNull String name) {
    if (!this.arguments.containsKey(name)) {
      throw new IllegalArgumentException("No argument with name " + name + " found");
    }
    return this.arguments.get(name);
  }

  /**
   * Get an argument by name and class.
   *
   * @param name the name of the argument
   * @param clazz the class of the argument
   * @param <T> the type of the argument
   * @return the argument cast to the specified class
   * @throws IllegalArgumentException if no argument with the name exists or if the argument is not
   *     of the specified class
   */
  @NonNull
  public <T> T get(@NonNull String name, @NonNull Class<T> clazz) {
    Object object = this.get(name);
    if (!clazz.isInstance(object)) {
      throw new IllegalArgumentException(
          "Argument " + name + " is not of type " + clazz.getSimpleName());
    }
    return clazz.cast(object);
  }

  /**
   * Get an extra argument by class.
   *
   * @param clazz the class of the argument
   * @return the argument cast to the specified class
   * @param <T> the type of the argument
   */
  public <T> T get(@NonNull Class<T> clazz) {
    for (Object object : this.extra) {
      if (clazz.isInstance(object)) {
        return clazz.cast(object);
      }
    }
    throw new IllegalArgumentException("No argument of type " + clazz.getSimpleName() + " found");
  }
}
