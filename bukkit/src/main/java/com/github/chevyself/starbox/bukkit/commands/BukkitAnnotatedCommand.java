package com.github.chevyself.starbox.bukkit.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.bukkit.BukkitAdapter;
import com.github.chevyself.starbox.bukkit.BukkitCommandExecutor;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.commands.AbstractAnnotatedCommand;
import com.github.chevyself.starbox.common.Async;
import java.lang.reflect.Method;
import lombok.Getter;
import lombok.NonNull;

/** Extension of {@link AbstractAnnotatedCommand} for bukkit commands. */
@Getter
public class BukkitAnnotatedCommand extends AbstractAnnotatedCommand<CommandContext, BukkitCommand>
    implements BukkitCommand {

  @NonNull private final BukkitAdapter adapter;
  @NonNull private final BukkitCommandExecutor executor;
  private final String permission;

  /**
   * Create a new annotated bukkit command.
   *
   * @param commandManager the command manager to register the command to
   * @param annotation the annotation of the command
   * @param object the object to invoke
   * @param method the method to invoke
   * @param adapter the bukkit adapter
   * @param permission the permission of the command
   */
  public BukkitAnnotatedCommand(
      @NonNull CommandManager<CommandContext, BukkitCommand> commandManager,
      @NonNull Command annotation,
      @NonNull Object object,
      @NonNull Method method,
      @NonNull BukkitAdapter adapter,
      String permission) {
    super(commandManager, annotation, object, method);
    this.adapter = adapter;
    this.permission = permission;
    this.executor =
        new BukkitCommandExecutor(
            this.getName(),
            Command.Supplier.getDescription(method),
            Command.Supplier.getUsage(method, this.getArguments(), annotation.aliases()),
            this.getAliases(),
            this,
            method.isAnnotationPresent(Async.class));
  }
}
