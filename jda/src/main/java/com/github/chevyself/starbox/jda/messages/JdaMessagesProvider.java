package com.github.chevyself.starbox.jda.messages;

import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.messages.MessagesProvider;
import lombok.NonNull;

/** Provides messages to results. */
public interface JdaMessagesProvider extends MessagesProvider<CommandContext> {

  /**
   * Get the message for when a command is not found.
   *
   * @param command is the input string that's not found as a command
   * @param context the context of the command
   * @return The message when a command is not found in {@link
   *     com.github.chevyself.starbox.CommandManager}
   */
  @NonNull
  String commandNotFound(@NonNull @Deprecated String command, @NonNull CommandContext context);

  /**
   * Get the message for when a user is not allowed to use a command.
   *
   * @param context the context of the command
   * @return the message when the sender does not have a permission
   */
  @NonNull
  String notAllowed(@NonNull CommandContext context);

  /**
   * Get the message for when a command may only be executed in a guild.
   *
   * @param context the context of the command
   * @return the message when the command has to be executed in a {@link
   *     net.dv8tion.jda.api.entities.Guild}
   */
  @NonNull
  String guildOnly(@NonNull CommandContext context);

  /**
   * The message sent when a string is not a valid user.
   *
   * @param string the string that is not valid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidUser(@NonNull String string, @NonNull CommandContext context);

  /**
   * The message sent when a string is not a valid user.
   *
   * @param string the string that is not valid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidMember(@NonNull String string, @NonNull CommandContext context);

  /**
   * The message sent when a string is not a valid role.
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidRole(@NonNull String string, @NonNull CommandContext context);

  /**
   * The message sent when a string is not a valid role.
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidTextChannel(String string, CommandContext context);

  /**
   * The message sent when a command was not executed from a {@link
   * net.dv8tion.jda.api.entities.Message}
   *
   * @param context the context of the command
   * @return the message to tell that the execution is wrong
   */
  String noMessage(@NonNull CommandContext context);
}
