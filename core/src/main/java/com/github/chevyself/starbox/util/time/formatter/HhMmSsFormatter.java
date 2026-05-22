package com.github.chevyself.starbox.util.time.formatter;

import java.time.Duration;
import lombok.Getter;
import lombok.NonNull;

/**
 * Formats time into the 'Hh:Mm:Ss' format:
 *
 * <p>If you give this formatter 'Time.forName("1d12m59s")'
 *
 * <p>The formatted {@link String} will be: "24:12:59"
 */
public class HhMmSsFormatter implements DurationFormatter {

  @NonNull @Getter private static final HhMmSsFormatter formatter = new HhMmSsFormatter();

  private HhMmSsFormatter() {}

  @Override
  public String format(@NonNull Duration duration) {
    long millis = Math.round(duration.toMillis());
    long secs = (millis / 1000) % 60;
    long minutes = (millis / (1000 * 60)) % 60;
    long hours = millis / (1000 * 60 * 60);
    return String.format("%02d:%02d:%02d", hours, minutes, secs);
  }
}
