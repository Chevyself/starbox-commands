package com.github.chevyself.starbox.providers;

import com.github.chevyself.starbox.arguments.ExtraArgument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import lombok.NonNull;

/**
 * This provider is used for {@link ExtraArgument} as it does not require an input, only the context
 * of the command execution.
 *
 * @param <O> the type of object to provide
 * @param <T> the type of context that this requires to provide the object
 */
public interface StarboxExtraArgumentProvider<O, T extends StarboxCommandContext<T, ?>>
    extends StarboxContextualProvider<O, T> {

  /**
   * Get the instance of the {@link #getClazz()} to provide.
   *
   * @param context the context of the command execution
   * @return a new instance of {@link O}
   * @throws ArgumentProviderException when the object could not be obtained. For example a
   *     TextChannel in Discord but the command was executed from a PrivateChannel
   */
  @NonNull
  O getObject(@NonNull T context) throws ArgumentProviderException;
}
