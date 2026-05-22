package com.github.chevyself.starbox.bungee.commands;

import com.github.chevyself.starbox.bungee.BungeeAdapter;
import com.github.chevyself.starbox.bungee.BungeeCommandExecutor;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.commands.StarboxCommand;
import lombok.NonNull;

/** Extension of the {@link StarboxCommand} for the Bungee platform. */
public interface BungeeCommand extends StarboxCommand<CommandContext, BungeeCommand> {

  /**
   * Get the executor for this command. This is used to register the command with the Bungee API.
   *
   * @return the executor
   */
  @NonNull
  BungeeCommandExecutor getExecutor();

  /**
   * Get the Bungee adapter for this command.
   *
   * @return the bungee adapter
   */
  @NonNull
  BungeeAdapter getAdapter();

  /**
   * Get the permission to run this command.
   *
   * @return the permission or null if none is required
   */
  String getPermission();
}
