package com.github.chevyself.starbox.exceptions;

import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.exceptions.type.StarboxRuntimeException;
import lombok.NonNull;

/**
 * This command is thrown by the {@link com.github.chevyself.starbox.CommandManager} when it cannot
 * register an {@link StarboxCommand}.
 */
public class CommandRegistrationException extends StarboxRuntimeException {

  /**
   * Create a simple exception with a simple message.
   *
   * @param message the message with the cause of the exception
   */
  public CommandRegistrationException(String message) {
    super(message);
  }

  /**
   * Create an exception with a message and a cause.
   *
   * @param message the message with the cause of the exception
   * @param cause the cause of the exception
   */
  public CommandRegistrationException(String message, @NonNull Throwable cause) {
    super(message, cause);
  }
}
