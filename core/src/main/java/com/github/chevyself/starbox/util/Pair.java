package com.github.chevyself.starbox.util;

import java.util.Objects;
import lombok.Getter;

/**
 * This utility class is used to return two objects from a method.
 *
 * @param <A> The type of the first object
 * @param <B> The type of the second object
 */
@Getter
public final class Pair<A, B> {

  private final A a;
  private final B b;

  /**
   * Create a new pair.
   *
   * @param a the first object
   * @param b the second object
   */
  public Pair(A a, B b) {
    this.a = a;
    this.b = b;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(a, pair.a) && Objects.equals(b, pair.b);
  }

  @Override
  public int hashCode() {
    return Objects.hash(a, b);
  }
}
