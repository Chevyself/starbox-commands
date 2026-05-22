package com.github.chevyself.starbox.jda.context;

import com.github.chevyself.starbox.jda.commands.JdaCommand;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

/**
 * Represents the context of a command executed via {@link
 * net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent}.
 */
public class SlashCommandContext implements CommandContext {

  @NonNull @Getter private final JDA jda;
  @NonNull @Getter private final CommandLineParser commandLineParser;
  @NonNull @Getter private final JdaCommand command;
  @NonNull @Getter private final User sender;
  @NonNull @Getter private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull @Getter private final MessagesProvider<CommandContext> messagesProvider;
  @NonNull @Getter private final SlashCommandInteractionEvent event;
  @NonNull @Getter private final List<OptionMapping> options;
  @NonNull private final MessageChannel channel;

  /**
   * Create the context.
   *
   * @param jda the jda instance of the command manager
   * @param commandLineParser the parser that parsed the command from the command line
   * @param command the command for which this context was created
   * @param sender the user that executed the command
   * @param providersRegistry the registry for the objects
   * @param messagesProvider the provider for messages
   * @param event the event that executed the command
   * @param options the options that are executing the command
   * @param channel the channel where the command was executed
   */
  public SlashCommandContext(
      @NonNull JDA jda,
      @NonNull CommandLineParser commandLineParser,
      @NonNull JdaCommand command,
      @NonNull User sender,
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider<CommandContext> messagesProvider,
      @NonNull SlashCommandInteractionEvent event,
      @NonNull List<OptionMapping> options,
      @NonNull MessageChannel channel) {
    this.jda = jda;
    this.commandLineParser = commandLineParser;
    this.command = command;
    this.sender = sender;
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
    this.event = event;
    this.options = options;
    this.channel = channel;
  }

  @Override
  public @NonNull Optional<MessageChannel> getChannel() {
    return Optional.of(channel);
  }

  @Override
  public @NonNull Optional<Message> getMessage() {
    return Optional.empty();
  }

  @Override
  public @NonNull SlashCommandContext getChildren(@NonNull JdaCommand command) {
    return new SlashCommandContext(
        this.jda,
        this.commandLineParser.copyFrom(1, command.getOptions()),
        command,
        this.sender,
        this.providersRegistry,
        this.messagesProvider,
        this.event,
        this.options,
        this.channel);
  }
}
