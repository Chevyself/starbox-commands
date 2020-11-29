package com.starfishst.core.messages;

import com.starfishst.core.arguments.MultipleArgument;
import com.starfishst.core.context.ICommandContext;
import lombok.NonNull;

/**
 * Provides messages for different instances of the manager
 *
 * @param <T> the command context
 */
public interface IMessagesProvider<T extends ICommandContext> {

  /**
   * The message sent when a string is not valid as a long
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell the user that the input is wrong
   */
  @NonNull
  String invalidLong(@NonNull String string, @NonNull T context);

  /**
   * The message sent when a string is not valid as a integer
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidInteger(@NonNull String string, @NonNull T context);

  /**
   * The message sent when a string is not valid as a double
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidDouble(@NonNull String string, @NonNull T context);

  /**
   * The message sent when a string is not valid as a boolean
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidBoolean(@NonNull String string, @NonNull T context);

  /**
   * The message sent when a string is not valid as Time
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidTime(@NonNull String string, @NonNull T context);

  /**
   * Get the message to send when there's a missing a argument
   *
   * @param name the name of the argument
   * @param description the description of the argument
   * @param position the position of the argument
   * @param context the context of the command
   * @return The error when the message is missing arguments
   */
  @NonNull
  String missingArgument(
      @NonNull String name, @NonNull String description, int position, T context);

  /**
   * Get the message to send when a string is not a valid number
   *
   * @param string the string that is not a valid number
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidNumber(@NonNull String string, @NonNull T context);

  /**
   * Get the message sent when the input string for a double is empty
   *
   * @param context the context of the command
   * @return the message to tell the user that a double cannot be empty
   */
  @NonNull
  String emptyDouble(@NonNull T context);

  /**
   * Get the message sent when a {@link MultipleArgument#getMinSize()} is bigger than the context
   * strings
   *
   * @param name the name of the argument
   * @param description the description of the argument
   * @param position the position of the argument
   * @param minSize the minimum size of the strings
   * @param missing how many strings are missing
   * @param context the context of the command
   * @return the message
   */
  @NonNull
  String missingStrings(
      @NonNull String name,
      @NonNull String description,
      int position,
      int minSize,
      int missing,
      @NonNull T context);
}
