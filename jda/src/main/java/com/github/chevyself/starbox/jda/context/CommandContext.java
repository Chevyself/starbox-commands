package com.github.chevyself.starbox.jda.context;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.jda.commands.JdaCommand;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import java.util.Optional;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

/** This context is used for every command {@link User being the sender}. */
public interface CommandContext extends StarboxCommandContext<CommandContext, JdaCommand> {

  @Override
  @NonNull
  JdaCommand getCommand();

  /**
   * Get the Discord instance in which the manager is running.
   *
   * @return the Discord instance
   */
  @NonNull
  JDA getJda();

  /**
   * Get the channel where the command was executed. This could be null when the command is executed
   * from console.
   *
   * @return the optional channel
   */
  @NonNull
  Optional<MessageChannel> getChannel();

  /**
   * Get the message which ran the command. This could be null when the command is executed from
   * console.
   *
   * @return the optional message
   */
  @NonNull
  Optional<Message> getMessage();

  @NonNull
  @Override
  User getSender();

  @Override
  @NonNull
  ProvidersRegistry<CommandContext> getProvidersRegistry();

  @Override
  @NonNull
  MessagesProvider<CommandContext> getMessagesProvider();

  @Override
  @NonNull
  CommandContext getChildren(@NonNull JdaCommand subcommand);
}
