package com.github.chevyself.starbox.exceptions;

import com.github.chevyself.starbox.providers.StarboxContextualProvider;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import lombok.NonNull;

/**
 * Thrown when a {@link StarboxContextualProvider} could not be registered in the {@link
 * ProvidersRegistry}.
 */
public class ArgumentProviderRegistrationException extends CommandRegistrationException {

  /**
   * Create the exception with a message.
   *
   * @param message the message of the exception
   */
  public ArgumentProviderRegistrationException(@NonNull String message) {
    super(message);
  }

  /**
   * Create the exception with a message and a cause.
   *
   * @param message the message of the exception
   * @param cause the cause of the exception
   */
  public ArgumentProviderRegistrationException(String message, @NonNull Throwable cause) {
    super(message, cause);
  }
}
