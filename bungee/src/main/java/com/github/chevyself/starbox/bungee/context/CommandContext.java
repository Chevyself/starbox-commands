package com.github.chevyself.starbox.bungee.context;

import com.github.chevyself.starbox.bungee.commands.BungeeCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import net.md_5.bungee.api.CommandSender;

/** The context for bungee commands. */
@Getter
public class CommandContext implements StarboxCommandContext<CommandContext, BungeeCommand> {

  @NonNull private final CommandLineParser commandLineParser;
  @NonNull private final BungeeCommand command;
  @NonNull private final CommandSender sender;
  @NonNull @Delegate private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull private final MessagesProvider<CommandContext> messagesProvider;

  /**
   * Create an instance.
   *
   * @param commandLineParser the parser that parsed the command from the command line
   * @param command the command for which this context was created
   * @param sender the sender of the command
   * @param providersRegistry the registry used in the context
   * @param messagesProvider the messages' provider for this context
   */
  public CommandContext(
      @NonNull CommandLineParser commandLineParser,
      @NonNull BungeeCommand command,
      @NonNull CommandSender sender,
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider<CommandContext> messagesProvider) {
    this.command = command;
    this.sender = sender;
    this.commandLineParser = commandLineParser;
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull CommandContext getChildren(@NonNull BungeeCommand subcommand) {
    return new CommandContext(
        commandLineParser.copyFrom(1, subcommand.getOptions()),
        subcommand,
        sender,
        providersRegistry,
        messagesProvider);
  }
}
