package com.github.chevyself.starbox.bukkit.context;

import com.github.chevyself.starbox.bukkit.commands.BukkitCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import org.bukkit.command.CommandSender;

/** The context of a Bukkit command. */
@Getter
public class CommandContext implements StarboxCommandContext<CommandContext, BukkitCommand> {

  @NonNull private final CommandLineParser commandLineParser;
  @NonNull private final BukkitCommand command;
  @NonNull private final CommandSender sender;
  @NonNull @Delegate private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull private final MessagesProvider<CommandContext> messagesProvider;

  /**
   * Create a Bukkit context.
   *
   * @param parser the parser that parsed the command from the command line
   * @param command the command for which this context was created
   * @param sender the sender that executed the command
   * @param providersRegistry the registry for the command context to use
   * @param messagesProvider the messages' provider used in this context
   */
  public CommandContext(
      @NonNull CommandLineParser parser,
      @NonNull BukkitCommand command,
      @NonNull CommandSender sender,
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider<CommandContext> messagesProvider) {
    this.commandLineParser = parser;
    this.command = command;
    this.sender = sender;
    this.messagesProvider = messagesProvider;
    this.providersRegistry = providersRegistry;
  }

  @Override
  public @NonNull CommandContext getChildren(@NonNull BukkitCommand subcommand) {
    return new CommandContext(
        this.commandLineParser.copyFrom(1, command.getOptions()),
        subcommand,
        this.sender,
        this.providersRegistry,
        this.messagesProvider);
  }
}
