package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.parsers.ParentCommandSupplier;
import com.github.chevyself.starbox.system.commands.AnnotatedSystemCommand;
import com.github.chevyself.starbox.system.commands.SystemCommand;
import com.github.chevyself.starbox.system.commands.SystemParentCommand;
import com.github.chevyself.starbox.system.context.CommandContext;
import java.lang.reflect.Method;
import lombok.NonNull;

/** Extension of {@link CommandParser} that is used for the System platform. */
public class SystemCommandParser extends CommandParser<CommandContext, SystemCommand> {

  /**
   * Create the command parser.
   *
   * @param adapter the adapter
   * @param commandManager the command manager
   */
  public SystemCommandParser(
      @NonNull SystemAdapter adapter,
      @NonNull CommandManager<CommandContext, SystemCommand> commandManager) {
    super(adapter, commandManager);
  }

  @Override
  public @NonNull ParentCommandSupplier<CommandContext, SystemCommand> getParentCommandSupplier() {
    return (annotation, clazz) -> new SystemParentCommand(annotation, commandManager);
  }

  @Override
  public @NonNull SystemCommand parseCommand(
      @NonNull Object object, @NonNull Method method, @NonNull Command annotation) {
    return new AnnotatedSystemCommand(annotation, object, method, commandManager);
  }
}
