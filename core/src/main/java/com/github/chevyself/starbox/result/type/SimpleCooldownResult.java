package com.github.chevyself.starbox.result.type;

import com.github.chevyself.starbox.result.CooldownResult;
import java.time.Duration;
import lombok.Getter;
import lombok.NonNull;

/** A simple implementation of {@link CooldownResult} and {@link SimpleResult}. */
@Getter
public class SimpleCooldownResult extends SimpleResult implements CooldownResult {

  @NonNull private final Duration cooldown;

  /**
   * Create the result.
   *
   * @param message the message to display
   * @param cooldown the cooldown to set
   */
  public SimpleCooldownResult(@NonNull String message, @NonNull Duration cooldown) {
    super(message);
    this.cooldown = cooldown;
  }
}
