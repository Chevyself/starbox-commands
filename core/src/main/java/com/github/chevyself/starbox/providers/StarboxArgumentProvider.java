package com.github.chevyself.starbox.providers;

import com.github.chevyself.starbox.arguments.SingleArgument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import lombok.NonNull;

/**
 * This provider is used for {@link SingleArgument}. As it only requires one input being the {@link
 * String} and as it is a {@link StarboxContextualProvider} requires the {@link
 * StarboxCommandContext}
 *
 * @param <O> the type of the object to provide
 * @param <T> the type of context that this requires to provide the object
 */
public interface StarboxArgumentProvider<O, T extends StarboxCommandContext<T, ?>>
    extends StarboxContextualProvider<O, T> {

  /**
   * Get the instance of the {@link #getClazz()} to provide.
   *
   * @param string the string to get the object from
   * @param context the context of the command execution
   * @return a new instance of {@link O}
   * @throws ArgumentProviderException when the object could not be obtained. For example getting a
   *     number from "A" is not possible and will cause a {@link NumberFormatException} which can be
   *     wrapped in a {@link ArgumentProviderException} to not {@link Throwable#printStackTrace()}
   *     but to tell the user to use a correct input
   */
  @NonNull
  O fromString(@NonNull String string, @NonNull T context) throws ArgumentProviderException;
}
