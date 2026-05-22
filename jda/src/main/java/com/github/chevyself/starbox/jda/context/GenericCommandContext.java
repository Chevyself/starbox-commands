package com.github.chevyself.starbox.jda.context;

import com.github.chevyself.starbox.jda.commands.JdaCommand;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/** This context is used for every command {@link User being the sender}. */
public class GenericCommandContext implements CommandContext {

  @NonNull @Getter protected final JDA jda;
  @NonNull @Getter protected final CommandLineParser commandLineParser;
  @NonNull @Getter protected final JdaCommand command;
  @NonNull @Getter protected final User sender;
  @NonNull @Getter protected final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull @Getter protected final MessagesProvider<CommandContext> messagesProvider;
  @NonNull @Getter protected final MessageReceivedEvent event;
  @NonNull protected final MessageChannel channel;
  protected final Message message;

  /**
   * Create an instance.
   *
   * @param jda the jda instance in which the {@link com.github.chevyself.starbox.jda.JdaAdapter} is
   *     registered
   * @param commandLineParser the parser that parsed the command from the command line
   * @param command the command for which this context was created
   * @param sender the sender of the command
   * @param providersRegistry the registry of the command context
   * @param messagesProvider the messages' provider for this context
   * @param event the event of the message that executes the command
   * @param channel the channel where the command was executed
   * @param message the message where the command was executed
   */
  public GenericCommandContext(
      @NonNull JDA jda,
      @NonNull CommandLineParser commandLineParser,
      @NonNull JdaCommand command,
      @NonNull User sender,
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider<CommandContext> messagesProvider,
      @NonNull MessageReceivedEvent event,
      @NonNull MessageChannel channel,
      Message message) {
    this.jda = jda;
    this.commandLineParser = commandLineParser;
    this.command = command;
    this.sender = sender;
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
    this.event = event;
    this.channel = channel;
    this.message = message;
  }

  @Override
  public @NonNull Optional<MessageChannel> getChannel() {
    return Optional.of(channel);
  }

  @NonNull
  public Optional<Message> getMessage() {
    return Optional.ofNullable(message);
  }

  @Override
  public @NonNull GenericCommandContext getChildren(@NonNull JdaCommand child) {
    return new GenericCommandContext(
        this.jda,
        this.commandLineParser.copyFrom(1, child.getOptions()),
        this.command,
        this.sender,
        this.providersRegistry,
        this.messagesProvider,
        this.event,
        this.channel,
        this.message);
  }

  @Override
  public String toString() {
    return "GenericCommandContext{"
        + "jda="
        + jda
        + ", commandLineParser="
        + commandLineParser
        + ", command="
        + command
        + ", sender="
        + sender
        + ", providersRegistry="
        + providersRegistry
        + ", messagesProvider="
        + messagesProvider
        + ", event="
        + event
        + ", channel="
        + channel
        + ", message="
        + message
        + '}';
  }
}
