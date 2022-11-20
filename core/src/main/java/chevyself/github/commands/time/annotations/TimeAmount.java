package chevyself.github.commands.time.annotations;

import chevyself.github.commands.time.Time;
import chevyself.github.commands.time.unit.StarboxUnit;
import chevyself.github.commands.time.unit.Unit;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import lombok.NonNull;

/** This represents {@link Time} as an annotation. */
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeAmount {

  /**
   * Get the value of the amount of time. This is used {@link Time#of(double, StarboxUnit)}
   *
   * @return the value of the amount of time
   */
  double amount() default 0;

  /**
   * Get the unit of the amount of time. This is used {@link Time#of(double, StarboxUnit)}
   *
   * @return the unit of the amount of time
   */
  Unit unit() default Unit.MILLIS;

  /**
   * Get the {@link String} to forName the amount of time instead of using {@link #amount()} and
   * {@link #unit()}.
   *
   * @return the {@link String} that can be parsed using {@link Time#parse(String, boolean)}
   */
  @NonNull
  String value() default "";
}