package com.github.chevyself.starbox.bungee.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.bungee.BungeeAdapter;
import com.github.chevyself.starbox.bungee.BungeeCommandExecutor;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.commands.AbstractAnnotatedCommand;
import java.lang.reflect.Method;
import lombok.Getter;
import lombok.NonNull;

/** Extension of the {@link AbstractAnnotatedCommand} for the Bungee platform. */
@Getter
public class BungeeAnnotatedCommand extends AbstractAnnotatedCommand<CommandContext, BungeeCommand>
    implements BungeeCommand {

  @NonNull private final BungeeAdapter adapter;
  @NonNull private final BungeeCommandExecutor executor;
  private final String permission;

  /**
   * Create a new annotated command.
   *
   * @param commandManager the command manager that registered this command
   * @param annotation the annotation
   * @param object the object to invoke
   * @param method the method to invoke
   * @param adapter the bungee adapter
   * @param permission the permission required to execute the command
   * @param async whether the command should be executed asynchronously
   */
  public BungeeAnnotatedCommand(
      @NonNull CommandManager<CommandContext, BungeeCommand> commandManager,
      @NonNull Command annotation,
      @NonNull Object object,
      @NonNull Method method,
      @NonNull BungeeAdapter adapter,
      String permission,
      boolean async) {
    super(commandManager, annotation, object, method);
    this.adapter = adapter;
    this.executor = new BungeeCommandExecutor(this, async);
    this.permission = permission;
  }
}
