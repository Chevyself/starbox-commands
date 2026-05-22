package com.github.chevyself.starbox.system.commands;

import com.github.chevyself.starbox.commands.AbstractBuiltCommand;
import com.github.chevyself.starbox.commands.CommandBuilder;
import com.github.chevyself.starbox.system.context.CommandContext;
import lombok.NonNull;

/** Extension of built command that is used for the System platform. */
public class SystemBuiltCommand extends AbstractBuiltCommand<CommandContext, SystemCommand>
    implements SystemCommand {

  /**
   * Create the built command.
   *
   * @param builder the builder
   */
  public SystemBuiltCommand(@NonNull CommandBuilder<CommandContext, SystemCommand> builder) {
    super(builder);
  }
}
