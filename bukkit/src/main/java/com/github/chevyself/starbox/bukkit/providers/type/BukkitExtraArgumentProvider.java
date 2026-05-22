package com.github.chevyself.starbox.bukkit.providers.type;

import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.providers.StarboxExtraArgumentProvider;

/**
 * An extension for bukkit extra arguments.
 *
 * @param <O> the type of the object to provide
 */
public interface BukkitExtraArgumentProvider<O>
    extends StarboxExtraArgumentProvider<O, CommandContext> {}
