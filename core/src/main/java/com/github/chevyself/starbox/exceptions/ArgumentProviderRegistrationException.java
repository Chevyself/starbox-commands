package com.github.chevyself.starbox.exceptions;

import lombok.NonNull;

/**
 * Thrown when a {@link com.github.chevyself.starbox.providers.type.StarboxContextualProvider} could
 * not be registered in the {@link
 * com.github.chevyself.starbox.providers.registry.ProvidersRegistry}.
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
