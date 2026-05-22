package com.github.chevyself.starbox.velocity.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.commands.AbstractParentCommand;
import com.github.chevyself.starbox.velocity.VelocityCommandExecutor;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import lombok.Getter;
import lombok.NonNull;

/** Extension of {@link AbstractParentCommand} for Velocity. */
@Getter
public class ParentVelocityCommand extends AbstractParentCommand<CommandContext, VelocityCommand>
    implements VelocityCommand {

  private final String permission;
  private final boolean async;
  @NonNull private final VelocityCommandExecutor executor;

  /**
   * Create the command.
   *
   * @param commandManager the command manager
   * @param annotation the annotation
   * @param permission the permission
   * @param async whether the command should be executed asynchronously
   */
  public ParentVelocityCommand(
      @NonNull CommandManager<CommandContext, VelocityCommand> commandManager,
      @NonNull Command annotation,
      String permission,
      boolean async) {
    super(commandManager, annotation);
    this.permission = permission;
    this.async = async;
    this.executor = new VelocityCommandExecutor(this);
  }
}
