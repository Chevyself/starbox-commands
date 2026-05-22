package com.github.chevyself.starbox.velocity;

import com.github.chevyself.starbox.common.tab.TabCompleter;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import com.github.chevyself.starbox.velocity.commands.VelocityCommand;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import com.github.chevyself.starbox.velocity.tab.VelocityTabCompleter;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;

/** Used to actually register the command with the Velocity API. */
public class VelocityCommandExecutor implements SimpleCommand {

  @NonNull
  private static final TabCompleter<CommandContext, VelocityCommand, CommandSource> tabCompleter =
      new VelocityTabCompleter();

  @NonNull private static final VelocityTabCompleter genericCompleter = new VelocityTabCompleter();
  @NonNull private final VelocityCommand command;

  /**
   * Create a new executor for the given command.
   *
   * @param command the command
   */
  public VelocityCommandExecutor(@NonNull VelocityCommand command) {
    this.command = command;
  }

  @Override
  public void execute(Invocation invocation) {
    CommandContext context =
        new CommandContext(
            CommandLineParser.parse(command.getOptions(), invocation.arguments()),
            command,
            invocation.source(),
            command.getProvidersRegistry(),
            command.getMessagesProvider());
    if (command.isAsync()) {
      CompletableFuture.runAsync(() -> command.execute(context));
    } else {
      command.execute(context);
    }
  }

  @Override
  public List<String> suggest(@NonNull Invocation invocation) {
    return VelocityCommandExecutor.tabCompleter.tabComplete(
        this.command, invocation.source(), invocation.alias(), invocation.arguments());
  }

  @Override
  public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
    return CompletableFuture.supplyAsync(
        () ->
            VelocityCommandExecutor.tabCompleter.tabComplete(
                this.command, invocation.source(), invocation.alias(), invocation.arguments()));
  }
}
