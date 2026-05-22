package com.github.chevyself.starbox.util.time.formatter;

import java.time.Duration;
import lombok.NonNull;

/**
 * Implementations of this class convert {@link Duration} into a readable {@link String} such as
 * {@link HhMmSsFormatter} which converts the given instance of time into 'Hh:Mm:Ss'.
 *
 * <p>Implement this class to format {@link Duration} at your liking
 *
 * @see HhMmSsFormatter
 */
public interface DurationFormatter {

  /**
   * Format the given time into a readable {@link String}.
   *
   * @param duration the instance of duration to convert
   * @return the formatted string
   */
  String format(@NonNull Duration duration);
}
