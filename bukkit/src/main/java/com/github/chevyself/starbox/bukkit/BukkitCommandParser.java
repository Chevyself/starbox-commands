package com.github.chevyself.starbox.bukkit;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.bukkit.commands.BukkitAnnotatedCommand;
import com.github.chevyself.starbox.bukkit.commands.BukkitCommand;
import com.github.chevyself.starbox.bukkit.commands.BukkitParentCommand;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.common.Async;
import com.github.chevyself.starbox.common.CommandPermission;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.parsers.ParentCommandSupplier;
import com.github.chevyself.starbox.util.ClassFinder;
import java.lang.reflect.Method;
import java.util.ArrayList;
import lombok.NonNull;

/** Parses commands for the bukkit platform. */
public class BukkitCommandParser extends CommandParser<CommandContext, BukkitCommand> {

  /**
   * Create a new command parser for the bukkit platform.
   *
   * @param adapter the bukkit adapter
   * @param commandManager the command manager
   */
  public BukkitCommandParser(
      @NonNull BukkitAdapter adapter,
      @NonNull CommandManager<CommandContext, BukkitCommand> commandManager) {
    super(adapter, commandManager);
  }

  @Override
  public @NonNull <O> ClassFinder<O> createClassFinder(
      Class<O> clazz, @NonNull String packageName) {
    return super.createClassFinder(clazz, packageName)
        .setClassLoaderSupplier(
            () -> ((BukkitAdapter) this.adapter).getPlugin().getClass().getClassLoader());
  }

  @Override
  public @NonNull ParentCommandSupplier<CommandContext, BukkitCommand> getParentCommandSupplier() {
    return (annotation, clazz) ->
        new BukkitParentCommand(
            this.commandManager,
            annotation,
            (BukkitAdapter) this.adapter,
            CommandPermission.Supplier.getPermission(clazz),
            Command.Supplier.getDescription(clazz),
            Command.Supplier.getUsage(clazz, new ArrayList<>(), annotation.aliases()),
            clazz.isAnnotationPresent(Async.class));
  }

  @Override
  public @NonNull BukkitCommand parseCommand(
      @NonNull Object object, @NonNull Method method, @NonNull Command annotation) {
    return new BukkitAnnotatedCommand(
        this.commandManager,
        annotation,
        object,
        method,
        (BukkitAdapter) adapter,
        CommandPermission.Supplier.getPermission(method));
  }
}
