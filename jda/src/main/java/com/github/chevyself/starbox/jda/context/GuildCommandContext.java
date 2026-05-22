package com.github.chevyself.starbox.jda.context;

import com.github.chevyself.starbox.jda.commands.JdaCommand;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * This context is used when the command is executed inside a guild. The context is still a {@link
 * User} but you can also get the {@link Member}
 */
@Getter
public class GuildCommandContext extends GenericCommandContext {

  /** The sender of the command as a member. */
  @NonNull private final Member member;
  /** The guild where the command was executed. */
  @NonNull private final Guild guild;

  /**
   * Create an instance.
   *
   * @param jda the jda instance in which the {@link com.github.chevyself.starbox.jda.JdaAdapter} is
   *     registered
   * @param commandLineParser the parser that parsed the command from the command line
   * @param command the command for which this context was created
   * @param sender the sender of the command
   * @param registry the registry of the command context
   * @param messagesProvider the messages' provider for this context
   * @param event the event of the message that executes the command
   * @param channel the channel where the command was executed
   * @param message the message where the command was executed
   */
  public GuildCommandContext(
      @NonNull JDA jda,
      @NonNull CommandLineParser commandLineParser,
      @NonNull JdaCommand command,
      @NonNull User sender,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull MessagesProvider<CommandContext> messagesProvider,
      @NonNull MessageReceivedEvent event,
      @NonNull MessageChannel channel,
      @NonNull Message message) {
    super(
        jda,
        commandLineParser,
        command,
        sender,
        registry,
        messagesProvider,
        event,
        channel,
        message);
    this.member =
        Objects.requireNonNull(
            message.getMember(), "Guild command context must have a valid member");
    this.guild = message.getGuild();
  }

  @Override
  public String toString() {
    return "GuildCommandContext{" + "member=" + this.member + ", guild=" + this.guild + '}';
  }

  @Override
  public @NonNull GuildCommandContext getChildren(@NonNull JdaCommand child) {
    CommandLineParser parse = CommandLineParser.parse(this.command.getOptions());
    return new GuildCommandContext(
        this.jda,
        this.commandLineParser.copyFrom(1, child.getOptions()),
        child,
        this.sender,
        this.providersRegistry,
        this.messagesProvider,
        this.event,
        this.channel,
        this.message);
  }
}
