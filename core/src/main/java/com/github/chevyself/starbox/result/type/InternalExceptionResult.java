package com.github.chevyself.starbox.result.type;

import lombok.NonNull;

/**
 * Represents a result from a command execution which resulted in an internal exception. This means
 * an exception was thrown which was not caused by the command sender, but the implementation of the
 * command.
 */
public class InternalExceptionResult extends ExceptionResult {

  /**
   * Create the result.
   *
   * @param throwable the exception thrown
   */
  public InternalExceptionResult(@NonNull Throwable throwable) {
    super(throwable);
  }
}
