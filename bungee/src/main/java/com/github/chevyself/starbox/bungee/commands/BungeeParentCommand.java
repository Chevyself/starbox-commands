package com.github.chevyself.starbox.bungee.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.bungee.BungeeAdapter;
import com.github.chevyself.starbox.bungee.BungeeCommandExecutor;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.commands.AbstractParentCommand;
import lombok.Getter;
import lombok.NonNull;

/** Extension of the {@link AbstractParentCommand} for the Bungee platform. */
@Getter
public class BungeeParentCommand extends AbstractParentCommand<CommandContext, BungeeCommand>
    implements BungeeCommand {

  @NonNull private final BungeeCommandExecutor executor;
  @NonNull private final BungeeAdapter adapter;
  private final String permission;

  /**
   * Create a new parent command.
   *
   * @param commandManager the command manager that registered this command
   * @param annotation the annotation
   * @param adapter the bungee adapter
   * @param permission the permission required to execute the command
   * @param async whether the command should be executed asynchronously
   */
  public BungeeParentCommand(
      @NonNull CommandManager<CommandContext, BungeeCommand> commandManager,
      @NonNull Command annotation,
      @NonNull BungeeAdapter adapter,
      String permission,
      boolean async) {
    super(commandManager, annotation);
    this.executor = new BungeeCommandExecutor(this, async);
    this.adapter = adapter;
    this.permission = permission;
  }
}
