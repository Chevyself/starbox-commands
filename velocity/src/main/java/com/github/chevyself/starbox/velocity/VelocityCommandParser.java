package com.github.chevyself.starbox.velocity;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.adapters.Adapter;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.common.Async;
import com.github.chevyself.starbox.common.CommandPermission;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.parsers.ParentCommandSupplier;
import com.github.chevyself.starbox.velocity.commands.AnnotatedVelocityCommand;
import com.github.chevyself.starbox.velocity.commands.ParentVelocityCommand;
import com.github.chevyself.starbox.velocity.commands.VelocityCommand;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import java.lang.reflect.Method;
import lombok.NonNull;

/** Extension of the {@link CommandParser} for the Velocity platform. */
public class VelocityCommandParser extends CommandParser<CommandContext, VelocityCommand> {

  /**
   * Create a new command parser for the Velocity platform.
   *
   * @param adapter the adapter
   * @param commandManager the command manager
   */
  public VelocityCommandParser(
      @NonNull Adapter<CommandContext, VelocityCommand> adapter,
      @NonNull CommandManager<CommandContext, VelocityCommand> commandManager) {
    super(adapter, commandManager);
  }

  @Override
  public @NonNull ParentCommandSupplier<CommandContext, VelocityCommand>
      getParentCommandSupplier() {
    return (annotation, clazz) ->
        new ParentVelocityCommand(
            this.commandManager,
            annotation,
            CommandPermission.Supplier.getPermission(clazz),
            clazz.isAnnotationPresent(Async.class));
  }

  @Override
  public @NonNull VelocityCommand parseCommand(
      @NonNull Object object, @NonNull Method method, @NonNull Command annotation) {
    return new AnnotatedVelocityCommand(
        this.commandManager,
        annotation,
        object,
        method,
        CommandPermission.Supplier.getPermission(method),
        method.isAnnotationPresent(Async.class));
  }
}
