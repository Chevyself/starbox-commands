package com.github.chevyself.starbox.result.type;

import com.github.chevyself.starbox.exceptions.type.StarboxException;
import lombok.NonNull;

/**
 * Represents a result from a command execution which resulted in an argument exception. This means
 * an exception was thrown which was caused by the command sender.
 */
public class ArgumentExceptionResult extends ExceptionResult {

  /**
   * Create the result.
   *
   * @param exception the exception thrown
   */
  public ArgumentExceptionResult(@NonNull StarboxException exception) {
    super(exception);
  }

  @Override
  public @NonNull String getMessage() {
    return this.exception.getMessage();
  }
}
