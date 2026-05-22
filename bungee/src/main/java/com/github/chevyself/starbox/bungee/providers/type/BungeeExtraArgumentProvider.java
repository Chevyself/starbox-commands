package com.github.chevyself.starbox.bungee.providers.type;

import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.providers.StarboxExtraArgumentProvider;

/**
 * An implementation for extra argument providers in bungee.
 *
 * @param <T> the type of object to provide
 */
public interface BungeeExtraArgumentProvider<T>
    extends StarboxExtraArgumentProvider<T, CommandContext> {}
