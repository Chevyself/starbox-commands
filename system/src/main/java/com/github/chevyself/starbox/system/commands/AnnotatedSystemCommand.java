package com.github.chevyself.starbox.system.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.commands.AbstractAnnotatedCommand;
import com.github.chevyself.starbox.system.context.CommandContext;
import java.lang.reflect.Method;
import lombok.NonNull;

/** Extension of {@link AbstractAnnotatedCommand} for system commands. */
public class AnnotatedSystemCommand extends AbstractAnnotatedCommand<CommandContext, SystemCommand>
    implements SystemCommand {

  /**
   * Create a new annotated system command.
   *
   * @param annotation the annotation of the command
   * @param object the object to invoke
   * @param method the method to invoke
   * @param commandManager the command manager to register the command to
   */
  public AnnotatedSystemCommand(
      @NonNull Command annotation,
      @NonNull Object object,
      @NonNull Method method,
      @NonNull CommandManager<CommandContext, SystemCommand> commandManager) {
    super(commandManager, annotation, object, method);
  }
}
