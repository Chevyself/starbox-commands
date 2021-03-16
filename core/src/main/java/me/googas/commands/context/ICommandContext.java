package me.googas.commands.context;

import lombok.NonNull;
import me.googas.commands.messages.IMessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.utility.Series;

/** The context of a command */
public interface ICommandContext {

  /**
   * Get's if the command was executed using a flag
   *
   * @param flag the flag to check
   * @return true if the command was executed with a flag
   */
  boolean hasFlag(@NonNull String flag);

  /**
   * Get the joined strings from a certain position
   *
   * @param position the position to get the string from
   * @return an array of strings empty if none
   */
  @NonNull
  default String[] getStringsFrom(int position) {
    return Series.arrayFrom(position, this.getStrings());
  }

  /**
   * Get the sender of the command
   *
   * @return the sender of the command
   */
  @NonNull
  Object getSender();

  /**
   * Get the joined strings of the command
   *
   * @return the joined strings
   */
  @NonNull
  String getString();

  /**
   * Get the joined strings of the command
   *
   * @return the joined strings
   */
  @NonNull
  String[] getStrings();

  /**
   * Get the registry used in this context
   *
   * @return the registry
   */
  ProvidersRegistry<? extends ICommandContext> getRegistry();

  /**
   * Get the messages provider used in this context
   *
   * @return the messages provider used in this context
   */
  IMessagesProvider<? extends ICommandContext> getMessagesProvider();
}
