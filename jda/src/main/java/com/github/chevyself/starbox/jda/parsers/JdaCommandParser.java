package com.github.chevyself.starbox.jda.parsers;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.jda.JdaAdapter;
import com.github.chevyself.starbox.jda.commands.JdaAnnotatedCommand;
import com.github.chevyself.starbox.jda.commands.JdaCommand;
import com.github.chevyself.starbox.jda.commands.JdaParentCommand;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.parsers.ParentCommandSupplier;
import java.lang.reflect.Method;
import lombok.NonNull;

public class JdaCommandParser extends CommandParser<CommandContext, JdaCommand> {

  public JdaCommandParser(
      @NonNull JdaAdapter adapter,
      @NonNull CommandManager<CommandContext, JdaCommand> commandManager) {
    super(adapter, commandManager);
  }

  @Override
  public @NonNull ParentCommandSupplier<CommandContext, JdaCommand> getParentCommandSupplier() {
    return (annotation, clazz) ->
        new JdaParentCommand(commandManager, annotation, Command.Supplier.getDescription(clazz));
  }

  @Override
  public @NonNull JdaCommand parseCommand(
      @NonNull Object object, @NonNull Method method, @NonNull Command annotation) {
    return new JdaAnnotatedCommand(commandManager, annotation, object, method);
  }
}
