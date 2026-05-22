package com.github.chevyself.starbox.jda.listener;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.jda.JdaAdapter;
import com.github.chevyself.starbox.jda.ListenerOptions;
import com.github.chevyself.starbox.jda.commands.JdaCommand;
import com.github.chevyself.starbox.jda.commands.UnknownCommand;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.context.GenericCommandContext;
import com.github.chevyself.starbox.jda.context.GuildCommandContext;
import com.github.chevyself.starbox.jda.context.SlashCommandContext;
import com.github.chevyself.starbox.jda.messages.JdaMessagesProvider;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import java.util.Arrays;
import lombok.Getter;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

/** The main listener of command execution. */
@Getter
public class CommandListener implements EventListener {

  @NonNull private final CommandManager<CommandContext, JdaCommand> manager;
  @NonNull private final JdaAdapter adapter;
  @NonNull private final JdaMessagesProvider messagesProvider;
  @NonNull private final ListenerOptions listenerOptions;

  /**
   * Create an instance.
   *
   * @param manager the command manager
   * @param messagesProvider the provider of messages
   * @param adapter the jda adapter
   * @param listenerOptions the options of the manager
   */
  public CommandListener(
      @NonNull CommandManager<CommandContext, JdaCommand> manager,
      @NonNull JdaMessagesProvider messagesProvider,
      @NonNull JdaAdapter adapter,
      @NonNull ListenerOptions listenerOptions) {
    this.manager = manager;
    this.adapter = adapter;
    this.listenerOptions = listenerOptions;
    this.messagesProvider = messagesProvider;
  }

  /**
   * On the event of a message received.
   *
   * @param event the event of a message received
   */
  @SubscribeEvent
  public void onMessageReceived(@NonNull MessageReceivedEvent event) {
    String[] strings = event.getMessage().getContentRaw().split(" +");
    String prefix = listenerOptions.getPrefix(event.isFromGuild() ? event.getGuild() : null);
    if (strings.length < 1 || !strings[0].startsWith(prefix)) {
      return;
    }
    String name = strings[0].substring(prefix.length());
    JdaCommand command = this.getCommand(event.isFromGuild() ? event.getGuild() : null, name);
    GenericCommandContext context = this.getCommandContext(event, strings, command);
    command.execute(context);
  }

  /**
   * Execute a command when the event is received.
   *
   * @param event the event of a slash command being received
   */
  @SubscribeEvent
  public void onSlashCommand(SlashCommandInteractionEvent event) {
    String name = event.getName();
    String[] strings =
        event.getOptions().stream().map(OptionMapping::getAsString).toArray(String[]::new);
    JdaCommand command = this.getCommand(event.isFromGuild() ? event.getGuild() : null, name);
    CommandLineParser parser = CommandLineParser.parse(command.getOptions(), strings);
    CommandContext context =
        new SlashCommandContext(
            event.getJDA(),
            parser,
            command,
            event.getUser(),
            this.manager.getProvidersRegistry(),
            this.messagesProvider,
            event,
            event.getOptions(),
            event.getChannel());
    command.execute(context);
  }

  @NonNull
  private JdaCommand getCommand(Guild guild, @NonNull String name) {
    return this.adapter
        .getCommand(guild, name)
        .orElse(new UnknownCommand(this.manager, name, this.messagesProvider));
  }

  /**
   * Get the context where the command was executed.
   *
   * @param event the event where the command was executed from
   * @param strings the strings representing the arguments of the command
   * @param commandName the name of the command
   * @return the context of the command
   */
  @NonNull
  private GenericCommandContext getCommandContext(
      @NonNull MessageReceivedEvent event, @NonNull String[] strings, @NonNull JdaCommand command) {
    strings = Arrays.copyOfRange(strings, 1, strings.length);
    CommandLineParser parser = CommandLineParser.parse(command.getOptions(), strings);
    if (event.isFromGuild()) {
      return new GuildCommandContext(
          adapter.getJda(),
          parser,
          command,
          event.getAuthor(),
          this.manager.getProvidersRegistry(),
          this.messagesProvider,
          event,
          event.getChannel(),
          event.getMessage());
    } else {
      return new GenericCommandContext(
          adapter.getJda(),
          parser,
          command,
          event.getAuthor(),
          this.manager.getProvidersRegistry(),
          this.messagesProvider,
          event,
          event.getChannel(),
          event.getMessage());
    }
  }

  @Override
  public void onEvent(@NonNull @NotNull final GenericEvent genericEvent) {
    if (genericEvent instanceof MessageReceivedEvent) {
      this.onMessageReceived((MessageReceivedEvent) genericEvent);
    } else if (genericEvent instanceof SlashCommandInteractionEvent) {
      this.onSlashCommand((SlashCommandInteractionEvent) genericEvent);
    }
  }
}
