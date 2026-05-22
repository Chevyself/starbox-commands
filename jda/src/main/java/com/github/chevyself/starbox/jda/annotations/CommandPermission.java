package com.github.chevyself.starbox.jda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;
import net.dv8tion.jda.api.Permission;

/** Represents the permission of a command. */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandPermission {

  /**
   * Get the permission to check for.
   *
   * @return the permission
   */
  @NonNull
  Permission value();
}
