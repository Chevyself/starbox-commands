package com.github.chevyself.starbox.jda.providers.type;

import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.providers.StarboxExtraArgumentProvider;

/**
 * An extension for an extra argument provider using JDA.
 *
 * @param <T> the type of object to provide
 */
public interface JdaExtraArgumentProvider<T>
    extends StarboxExtraArgumentProvider<T, CommandContext> {}
