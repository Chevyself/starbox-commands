package com.github.chevyself.starbox.result;

import java.time.Duration;
import lombok.NonNull;

/** Implementing classes represent a result from a command execution which sets a cooldown. */
public interface CooldownResult extends Result {

  /**
   * Get the cooldown to set.
   *
   * @return the cooldown to set
   */
  @NonNull
  Duration getCooldown();
}
