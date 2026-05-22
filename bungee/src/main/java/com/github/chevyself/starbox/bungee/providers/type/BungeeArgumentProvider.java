package com.github.chevyself.starbox.bungee.providers.type;

import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.common.tab.SuggestionsArgumentProvider;

/**
 * A provider made for bungee commands.
 *
 * @param <O> the type of the object to provider
 */
public interface BungeeArgumentProvider<O> extends SuggestionsArgumentProvider<O, CommandContext> {}
