package com.github.chevyself.starbox.annotations;

import com.github.chevyself.starbox.commands.StarboxCommand;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to represent the parent commands that are parsed using reflection
 *
 * <p>Lets suppose to have the prefix '/'. If you have the {@link StarboxCommand} 'hello' you can
 * parseAndRegister other {@link StarboxCommand} using {@link
 * StarboxCommand#addChild(StarboxCommand)} and the command execution with be as follows:
 *
 * <p>/hello &lt;command&gt; &lt;**args&gt;
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Parent {}
