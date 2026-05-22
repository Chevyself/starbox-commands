package com.github.chevyself.starbox.bukkit.commands;

import com.github.chevyself.starbox.bukkit.BukkitAdapter;
import com.github.chevyself.starbox.bukkit.BukkitCommandExecutor;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.commands.StarboxCommand;
import lombok.NonNull;

/** Represents a Starbox command made for bukkit. */
public interface BukkitCommand extends StarboxCommand<CommandContext, BukkitCommand> {

  /**
   * Get the bukkit adapter.
   *
   * @return the bukkit adapter
   */
  @NonNull
  BukkitAdapter getAdapter();

  /**
   * Get the bukkit command executor.
   *
   * @return the bukkit command executor
   */
  @NonNull
  BukkitCommandExecutor getExecutor();

  /**
   * Get the required permission to run the command.
   *
   * @return the required permission to run the command or null if none
   */
  String getPermission();
}
