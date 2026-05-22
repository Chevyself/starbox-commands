package me.googas.samples;

import com.github.chevyself.starbox.util.time.TimeUtil;
import java.time.Duration;

public class TimeUtilTest {

  public static void main(String[] args) {
    Duration duration = TimeUtil.durationOf("1o1o10o1y");
    System.out.println(duration);
    System.out.println(duration.getUnits());
    System.out.println(TimeUtil.toString(duration));
    System.out.println(TimeUtil.durationOf("0s").getUnits());
  }
}
