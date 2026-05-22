package com.github.chevyself.starbox.jda.providers.type;

import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.providers.StarboxArgumentProvider;

/**
 * An extension for an argument provider using JDA.
 *
 * @param <T> the type of object to provide
 */
public interface JdaArgumentProvider<T> extends StarboxArgumentProvider<T, CommandContext> {}
