package com.github.chevyself.starbox.bungee.messages;

import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.messages.MessagesProvider;
import lombok.NonNull;

/** The messages provider for bungee. */
public interface BungeeMessagesProvider extends MessagesProvider<CommandContext> {

  /**
   * The message sent when the user that executed the command is not allowed to use it.
   *
   * @param context the context of the command
   * @return the message to send
   */
  @NonNull
  String notAllowed(CommandContext context);

  /**
   * The message to send when the string does not match a proxied player.
   *
   * @param string the string
   * @param context the context of the command
   * @return the message to send
   */
  @NonNull
  String invalidPlayer(@NonNull String string, @NonNull CommandContext context);

  /**
   * The message to send when a command that is required that the sender is a player happens to be.
   * something else
   *
   * @param context the context of the command
   * @return the message
   */
  @NonNull
  String onlyPlayers(CommandContext context);
}
