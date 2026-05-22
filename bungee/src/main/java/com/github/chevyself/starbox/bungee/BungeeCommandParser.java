package com.github.chevyself.starbox.bungee;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.bungee.commands.BungeeAnnotatedCommand;
import com.github.chevyself.starbox.bungee.commands.BungeeCommand;
import com.github.chevyself.starbox.bungee.commands.BungeeParentCommand;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.common.Async;
import com.github.chevyself.starbox.common.CommandPermission;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.parsers.ParentCommandSupplier;
import java.lang.reflect.Method;
import lombok.NonNull;

/** Implementation of {@link CommandParser} for the Bungee platform. */
public class BungeeCommandParser extends CommandParser<CommandContext, BungeeCommand> {

  /**
   * Create a new command parser.
   *
   * @param adapter the bungee adapter
   * @param commandManager the command manager
   */
  public BungeeCommandParser(
      @NonNull BungeeAdapter adapter,
      @NonNull CommandManager<CommandContext, BungeeCommand> commandManager) {
    super(adapter, commandManager);
  }

  @Override
  public @NonNull ParentCommandSupplier<CommandContext, BungeeCommand> getParentCommandSupplier() {
    return (annotation, clazz) ->
        new BungeeParentCommand(
            this.commandManager,
            annotation,
            (BungeeAdapter) this.adapter,
            CommandPermission.Supplier.getPermission(clazz),
            clazz.isAnnotationPresent(Async.class));
  }

  @Override
  public @NonNull BungeeCommand parseCommand(
      @NonNull Object object, @NonNull Method method, @NonNull Command annotation) {
    return new BungeeAnnotatedCommand(
        this.commandManager,
        annotation,
        object,
        method,
        (BungeeAdapter) this.adapter,
        CommandPermission.Supplier.getPermission(method),
        method.isAnnotationPresent(Async.class));
  }
}
