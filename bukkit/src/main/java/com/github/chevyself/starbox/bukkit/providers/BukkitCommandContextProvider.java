package com.github.chevyself.starbox.bukkit.providers;

import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.providers.type.BukkitExtraArgumentProvider;
import com.github.chevyself.starbox.providers.type.CommandContextProvider;
import lombok.NonNull;

/**
 * Provides the {@link com.github.chevyself.starbox.CommandManager} with the object {@link
 * CommandContext}.
 */
public class BukkitCommandContextProvider extends CommandContextProvider<CommandContext>
    implements BukkitExtraArgumentProvider<CommandContext> {

  @Override
  public @NonNull Class<CommandContext> getClazz() {
    return CommandContext.class;
  }
}
