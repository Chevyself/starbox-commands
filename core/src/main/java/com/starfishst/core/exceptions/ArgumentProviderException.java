package com.starfishst.core.exceptions;

import com.starfishst.core.exceptions.type.SimpleException;
import lombok.NonNull;

/**
 * This exception is thrown when an argument is going to return null (they must return the object)
 */
public class ArgumentProviderException extends SimpleException {

  /**
   * Throw the exception using a message with place holders
   *
   * @param message the message
   */
  public ArgumentProviderException(@NonNull String message) {
    super(message);
  }
}
