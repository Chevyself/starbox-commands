package com.github.chevyself.starbox.providers;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import lombok.NonNull;

/**
 * This provider requires of a context to provide an object. This means that in order to provide the
 * argument for a command requires of the context in the command execution.
 *
 * <p>The direct implementations are:
 *
 * <ul>
 *   <li>{@link StarboxArgumentProvider}
 *   <li>{@link StarboxExtraArgumentProvider}
 * </ul>
 *
 * @param <O> the type of object to provide
 * @param <T> the type of context that this requires to provide the object
 */
public interface StarboxContextualProvider<O, T extends StarboxCommandContext<T, ?>> {
  /**
   * Get if the provider provides with the queried class.
   *
   * <p>By default this will check using {@link #getClazz()} {@link Class#isAssignableFrom(Class)}
   *
   * @param clazz the queried class
   * @return true if it provides it
   */
  default boolean provides(@NonNull Class<?> clazz) {
    return clazz.isAssignableFrom(this.getClazz());
  }

  /**
   * Get the class to provide.
   *
   * @return the class to provide
   */
  @NonNull
  Class<O> getClazz();
}
