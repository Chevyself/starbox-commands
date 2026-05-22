package com.github.chevyself.starbox.result;

import com.github.chevyself.starbox.result.type.SimpleCooldownResult;
import com.github.chevyself.starbox.result.type.SimpleResult;
import java.time.Duration;
import lombok.NonNull;

/** Implementing classes represent a result from a command execution. */
public interface Result {

  /**
   * Create a result to display a message to the command sender.
   *
   * @param string the message to display
   * @return a result to display a message to the command sender
   */
  @NonNull
  static Result of(@NonNull String string) {
    return new SimpleResult(string);
  }

  /**
   * Create a result to display a message to the command sender and set a cooldown.
   *
   * @param string the message to display
   * @param cooldown the cooldown to set
   * @return a result to display a message to the command sender and set a cooldown
   */
  @NonNull
  static Result of(@NonNull String string, @NonNull Duration cooldown) {
    return new SimpleCooldownResult(string, cooldown);
  }
}
