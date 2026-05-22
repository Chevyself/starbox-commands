package com.github.chevyself.starbox.system.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.commands.AbstractParentCommand;
import com.github.chevyself.starbox.system.context.CommandContext;
import lombok.NonNull;

/** Extension of {@link AbstractParentCommand} that is used for the System platform. */
public class SystemParentCommand extends AbstractParentCommand<CommandContext, SystemCommand>
    implements SystemCommand {

  /**
   * Create the parent command.
   *
   * @param annotation the annotation
   * @param commandManager the command manager
   */
  public SystemParentCommand(
      @NonNull Command annotation,
      @NonNull CommandManager<CommandContext, SystemCommand> commandManager) {
    super(commandManager, annotation);
  }
}
