package com.github.chevyself.starbox.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation targets a class that contains command methods.
 *
 * @see com.github.chevyself.starbox.CommandManager#registerAllIn(String)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandCollection {}
