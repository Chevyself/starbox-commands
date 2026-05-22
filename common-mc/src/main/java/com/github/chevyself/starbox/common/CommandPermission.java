package com.github.chevyself.starbox.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import lombok.NonNull;

/** Represents the permission of a command. */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface CommandPermission {

  /**
   * Get the permission node.
   *
   * @return the permisson node
   */
  @NonNull
  String value();

  /** Utility class for this annotation. */
  final class Supplier {

    /**
     * Get the permission node from an {@link AnnotatedElement}. If the element is not annotated
     * with {@link CommandPermission} then null is returned.
     *
     * @param element the element to get the permission from
     * @return the permission node or null
     */
    public static String getPermission(@NonNull AnnotatedElement element) {
      if (element.isAnnotationPresent(CommandPermission.class)) {
        return element.getAnnotation(CommandPermission.class).value();
      }
      return null;
    }
  }
}
