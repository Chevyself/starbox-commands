package com.github.chevyself.starbox.bungee.tab;

import com.github.chevyself.starbox.bungee.commands.BungeeCommand;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.common.tab.GenericTabCompleter;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;

/** Extension of {@link GenericTabCompleter} for Bungee commands. */
public class BungeeTabCompleter
    extends GenericTabCompleter<CommandContext, BungeeCommand, CommandSender> {

  @Override
  public String getPermission(@NonNull BungeeCommand command) {
    return command.getPermission();
  }

  @Override
  public boolean hasPermission(@NonNull CommandSender sender, @NonNull String permission) {
    return sender.hasPermission(permission);
  }

  @Override
  public @NonNull CommandContext createContext(
      @NonNull CommandLineParser parser,
      @NonNull BungeeCommand command,
      @NonNull CommandSender sender) {
    return new CommandContext(
        parser, command, sender, command.getProvidersRegistry(), command.getMessagesProvider());
  }
}
