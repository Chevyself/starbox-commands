package com.github.chevyself.starbox.bukkit.commands;

import com.github.chevyself.starbox.bukkit.BukkitAdapter;
import com.github.chevyself.starbox.bukkit.BukkitCommandExecutor;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.commands.StarboxCommand;
import lombok.NonNull;

public interface BukkitCommand extends StarboxCommand<CommandContext, BukkitCommand> {

  @NonNull
  BukkitAdapter getAdapter();

  @NonNull
  BukkitCommandExecutor getExecutor();

  String getPermission();
}