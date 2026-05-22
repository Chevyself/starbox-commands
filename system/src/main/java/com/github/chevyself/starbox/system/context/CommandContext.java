package com.github.chevyself.starbox.system.context;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import com.github.chevyself.starbox.system.commands.SystemCommand;
import com.github.chevyself.starbox.system.context.sender.CommandSender;
import com.github.chevyself.starbox.system.context.sender.ConsoleCommandSender;
import lombok.Getter;
import lombok.NonNull;

/**
 * This context to execute {@link SystemCommand}. This does not include anything different from
 * {@link StarboxCommandContext} the {@link #sender} is the static instance of {@link
 * ConsoleCommandSender#INSTANCE}
 */
@Getter
public class CommandContext implements StarboxCommandContext<CommandContext, SystemCommand> {

  @NonNull private final CommandLineParser commandLineParser;
  @NonNull private final SystemCommand command;
  @NonNull private final CommandSender sender;
  @NonNull private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull private final MessagesProvider<CommandContext> messagesProvider;

  /**
   * Create the command context.
   *
   * @param commandLineParser the parser that parsed the command from the command line
   * @param command for which this context was created
   * @param sender the static instance of {@link ConsoleCommandSender#INSTANCE}
   * @param providersRegistry the registry to parse {@link Object} array to run {@link
   *     SystemCommand}
   * @param messagesProvider the messages provider for messages of providers
   */
  public CommandContext(
      @NonNull CommandLineParser commandLineParser,
      @NonNull SystemCommand command,
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
  public @NonNull CommandContext getChildren(@NonNull SystemCommand subcommand) {
    return new CommandContext(
        this.commandLineParser.copyFrom(1, subcommand.getOptions()),
        subcommand,
        this.sender,
        this.providersRegistry,
        this.messagesProvider);
  }
}
