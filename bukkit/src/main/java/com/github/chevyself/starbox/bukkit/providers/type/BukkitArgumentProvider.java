package com.github.chevyself.starbox.bukkit.providers.type;

import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.common.tab.SuggestionsArgumentProvider;

/** An extension for providers made for bukkit commands. */
public interface BukkitArgumentProvider<O> extends SuggestionsArgumentProvider<O, CommandContext> {}
