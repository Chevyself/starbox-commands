package com.github.chevyself.starbox.velocity.tab;

import com.github.chevyself.starbox.common.tab.GenericTabCompleter;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import com.github.chevyself.starbox.velocity.commands.VelocityCommand;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import lombok.NonNull;

/** Extension of the {@link GenericTabCompleter} for the Velocity platform. */
public class VelocityTabCompleter
    extends GenericTabCompleter<CommandContext, VelocityCommand, CommandSource> {

  @Override
  public String getPermission(@NonNull VelocityCommand command) {
    return command.getPermission();
  }

  @Override
  public boolean hasPermission(@NonNull CommandSource sender, @NonNull String permission) {
    return sender.hasPermission(permission);
  }

  @Override
  public @NonNull CommandContext createContext(
      @NonNull CommandLineParser parser,
      @NonNull VelocityCommand command,
      @NonNull CommandSource sender) {
    return new CommandContext(
        parser, command, sender, command.getProvidersRegistry(), command.getMessagesProvider());
  }
}
