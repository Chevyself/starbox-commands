package com.github.chevyself.starbox.exceptions;

import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.commands.ReflectCommand;
import com.github.chevyself.starbox.exceptions.type.StarboxException;
import lombok.NonNull;

/**
 * This exception is thrown by {@link ReflectCommand} when an {@link Argument} is missing from the
 * execution.
 *
 * <p>If you have a command with the next usage:
 *
 * <p>[prefix][command] [{@link Required} String name]
 *
 * <p>Let's suppose to have '/' as the prefix, the command 'hello', and the user runs:
 *
 * <p>/hello
 *
 * <p>An exception will be thrown as the {@link Required} argument is missing. The proper execution
 * of the command will be:
 *
 * <p>/hello world!
 */
public class MissingArgumentException extends StarboxException {

  /**
   * Create a simple exception with a simple message.
   *
   * @param message the message with the cause of the exception
   */
  public MissingArgumentException(@NonNull String message) {
    super(message);
  }
}
