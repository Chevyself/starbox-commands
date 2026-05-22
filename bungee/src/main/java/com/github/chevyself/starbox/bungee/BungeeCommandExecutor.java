package com.github.chevyself.starbox.bungee;

import com.github.chevyself.starbox.bungee.commands.BungeeCommand;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.bungee.tab.BungeeTabCompleter;
import com.github.chevyself.starbox.common.Aliases;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

/**
 * Represents the actual command executor for the Bungee platform. This is used to register the
 * command with the Bungee API.
 */
public class BungeeCommandExecutor extends Command implements TabExecutor {

  @NonNull private static final BungeeTabCompleter tabCompleter = new BungeeTabCompleter();
  @NonNull private final BungeeCommand command;
  private final boolean async;

  /**
   * Create a new command executor.
   *
   * @param command the command to execute
   * @param async whether the command should be executed asynchronously
   */
  public BungeeCommandExecutor(@NonNull BungeeCommand command, boolean async) {
    super(command.getName(), command.getPermission(), Aliases.getAliases(command));
    this.command = command;
    this.async = async;
  }

  @Override
  public void execute(CommandSender sender, String[] strings) {
    CommandLineParser parser = CommandLineParser.parse(command.getOptions(), strings);
    CommandContext context =
        new CommandContext(
            parser, command, sender, command.getProvidersRegistry(), command.getMessagesProvider());
    if (this.async) {
      ProxyServer.getInstance()
          .getScheduler()
          .runAsync(command.getAdapter().getPlugin(), () -> command.execute(context));
    } else {
      command.execute(context);
    }
  }

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] strings) {
    return BungeeCommandExecutor.tabCompleter.tabComplete(command, sender, this.getName(), strings);
  }
}
