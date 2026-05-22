package com.github.chevyself.starbox.util.time;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NonNull;

/** This class contains static methods with utility for the 'java.time' package. */
public class TimeUtil {

  @NonNull public static final Map<Character, ChronoUnit> UNITS_CHARS = new HashMap<>();
  @NonNull public static final List<ChronoUnit> UNITS;

  static {
    TimeUtil.UNITS_CHARS.put('L', ChronoUnit.MILLIS);
    TimeUtil.UNITS_CHARS.put('S', ChronoUnit.SECONDS);
    TimeUtil.UNITS_CHARS.put('M', ChronoUnit.MINUTES);
    TimeUtil.UNITS_CHARS.put('H', ChronoUnit.HOURS);
    TimeUtil.UNITS_CHARS.put('D', ChronoUnit.DAYS);
    TimeUtil.UNITS_CHARS.put('W', ChronoUnit.WEEKS);
    TimeUtil.UNITS_CHARS.put('O', ChronoUnit.MONTHS);
    TimeUtil.UNITS_CHARS.put('Y', ChronoUnit.YEARS);

    UNITS = TimeUtil.UNITS_CHARS.values().stream().sorted().collect(Collectors.toList());
  }

  /**
   * Parses a duration from the string. The string from {@link TimeUtil#toString(Duration)} can be
   * used in this method.
   *
   * <p>This works as follows: [value][{@link #unitOf(char)}] the unit ignores casing.
   *
   * <p>So:
   *
   * <ul>
   *   <li>'2s' = 2 SECONDS
   *   <li>'1y' = 1 YEARS
   *   <li>'2.5d' = 2.5 DAYS
   * </ul>
   *
   * <p>You can also chain values:
   *
   * <ul>
   *   <li>'2s2s2s2s2s' = 10 SECONDS
   *   <li>'1o2w' = 1.5 MONTHS
   *   <li>'2y6o2w' = 2.538 YEARS
   * </ul>
   *
   * @param string the string to parse
   * @return the parsed instance of duration
   * @throws IllegalArgumentException if no unit is matched
   */
  @NonNull
  public static Duration durationOf(@NonNull String string) {
    if (string.isEmpty()) {
      throw new IllegalArgumentException("Received an empty string");
    }
    long millis = 0;
    int state = 0;
    int last = 0;
    double value = 0;
    for (int i = 0; i < string.length(); i++) {
      char c = string.charAt(i);
      if (Character.isDigit(c) || c == '.') {
        if (state == 2) {
          millis += (long) (TimeUtil.unitOf(string.charAt(last)).getDuration().toMillis() * value);
          last = i;
        }
        state = 1;
      } else {
        if (state == 1) {
          value = Double.parseDouble(string.substring(last, i));
          last = i;
        }
        state = 2;
      }
    }
    millis += (long) (TimeUtil.unitOf(string.charAt(last)).getDuration().toMillis() * value);
    return Duration.ofMillis(millis);
  }

  /**
   * Get the unit that matches the character. This means that:
   *
   * <ul>
   *   <li>'L' = {@link ChronoUnit#MILLIS}
   *   <li>'S' = {@link ChronoUnit#SECONDS}
   *   <li>'M' = {@link ChronoUnit#MINUTES}
   *   <li>'H' = {@link ChronoUnit#HOURS}
   *   <li>'D' = {@link ChronoUnit#DAYS}
   *   <li>'W' = {@link ChronoUnit#WEEKS}
   *   <li>'O' = {@link ChronoUnit#MONTHS}
   *   <li>'Y' = {@link ChronoUnit#YEARS}
   * </ul>
   *
   * @param c the character to match the unit to
   * @return the unit that matches the character
   * @throws IllegalArgumentException if the character does not match any unit
   */
  @NonNull
  public static ChronoUnit unitOf(char c) {
    ChronoUnit unit = TimeUtil.UNITS_CHARS.get(Character.toUpperCase(c));
    if (unit == null) {
      throw new IllegalArgumentException("Could not match a ChronoUnit for " + c);
    }
    return unit;
  }

  /**
   * Convert a unit into a {@link String}.
   *
   * @see #durationOf(String)
   * @param duration the duration to convert
   * @return the string
   */
  @NonNull
  public static String toString(@NonNull Duration duration) {
    if (duration.isNegative() || duration.isZero()) {
      return "0S";
    }
    long millis = duration.toMillis();
    ChronoUnit current = TimeUtil.fromMillis(millis);
    StringBuilder builder = new StringBuilder();
    List<ChronoUnit> list = new ArrayList<>(TimeUtil.UNITS);
    Collections.reverse(list);
    for (ChronoUnit unit : list) {
      long unitMillis = unit.getDuration().toMillis();
      if (unitMillis <= current.getDuration().toMillis()) {
        long value = millis / unitMillis;
        if (value <= 0) {
          continue;
        }
        millis -= value * unitMillis;
        builder.append(value).append(Character.toLowerCase(TimeUtil.getChar(unit)));
      }
    }
    return builder.toString();
  }

  /**
   * Get the character that matches the unit.
   *
   * @param unit the unit to match the character with
   * @return the character that matches
   * @throws IllegalArgumentException if not character matches the unit
   */
  public static char getChar(@NonNull ChronoUnit unit) {
    return TimeUtil.UNITS_CHARS.entrySet().stream()
        .filter(entry -> entry.getValue().equals(unit))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Could not match character for " + unit))
        .getKey();
  }

  /**
   * Get the unit that matches the given millis. This will attempt to get the unit with the closer
   * {@link ChronoUnit#getDuration()} {@link Duration#toMillis()}
   *
   * <p>This means that:
   *
   * <ul>
   *   <li>5000 = {@link ChronoUnit#SECONDS}
   *   <li>31567800000 = {@link ChronoUnit#YEARS}
   *   <li>80000 = {@link ChronoUnit#MINUTES}
   * </ul>
   *
   * @param millis the millis to match the unit to
   * @return the unit that matches the millis
   * @throws IllegalArgumentException if millis is lower than 0
   */
  @NonNull
  public static ChronoUnit fromMillis(long millis) {
    if (millis < 0) {
      throw new IllegalArgumentException("millis must be higher than 0");
    }
    ChronoUnit unit = ChronoUnit.MILLIS;
    for (ChronoUnit value : TimeUtil.UNITS) {
      if (value.getDuration().toMillis() <= millis) {
        unit = value;
      }
    }
    return unit;
  }
}
