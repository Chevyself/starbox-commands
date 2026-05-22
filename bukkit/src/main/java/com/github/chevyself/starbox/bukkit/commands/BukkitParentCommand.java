package com.github.chevyself.starbox.bukkit.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.bukkit.BukkitAdapter;
import com.github.chevyself.starbox.bukkit.BukkitCommandExecutor;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.commands.AbstractParentCommand;
import lombok.Getter;
import lombok.NonNull;

/** Represents a parent command for the bukkit platform. */
@Getter
public class BukkitParentCommand extends AbstractParentCommand<CommandContext, BukkitCommand>
    implements BukkitCommand {

  @NonNull private final BukkitAdapter adapter;
  @NonNull private final BukkitCommandExecutor executor;
  private final String permission;

  /**
   * Creates a new parent command for the bukkit platform.
   *
   * @param commandManager the command manager
   * @param annotation the annotation
   * @param adapter the bukkit adapter
   * @param permission the permission
   * @param description the description
   * @param usage the usage
   * @param async whether the command should be executed asynchronously
   */
  public BukkitParentCommand(
      @NonNull CommandManager<CommandContext, BukkitCommand> commandManager,
      @NonNull Command annotation,
      @NonNull BukkitAdapter adapter,
      String permission,
      String description,
      String usage,
      boolean async) {
    super(commandManager, annotation);
    this.adapter = adapter;
    this.permission = permission;
    this.executor =
        new BukkitCommandExecutor(
            this.getName(), description, usage, this.getAliases(), this, async);
  }
}
