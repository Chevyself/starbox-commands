package com.github.chevyself.starbox.context;

import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.flags.FlagArgument;
import com.github.chevyself.starbox.flags.StarboxFlag;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import java.util.Optional;
import lombok.NonNull;

/**
 * The context of the execution of a command. The principal variables that a context requires is the
 * sender and the strings that represent the arguments, which are given by the {@link
 * #getCommandLineParser()}. Each platform has a different context.
 *
 * @param <C> the type of context
 * @param <T> the type of the command
 */
public interface StarboxCommandContext<
    C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  /**
   * Get a flag with its alias. This will first attempt to get a {@link FlagArgument} if it is not
   * present in this context it will try to get it from the command using {@link
   * StarboxCommand#getOption(String)}. Flags will be matched using {@link
   * StarboxFlag#hasAlias(String)}
   *
   * @param alias the alias to match
   * @return an optional which may hold the flag
   */
  @NonNull
  default Optional<? extends StarboxFlag> getFlag(@NonNull String alias) {
    Optional<FlagArgument> optional =
        this.getCommandLineParser().getFlags().stream()
            .filter(flag -> flag.hasAlias(alias))
            .findFirst();
    return optional.isPresent() ? optional : this.getCommand().getOption(alias);
  }

  /**
   * Get the value of a flag. This will get the flag using {@link #getFlag(String)}
   *
   * @param alias the alias of the flag to get
   * @return an optional which may contain the value or not
   */
  @NonNull
  default Optional<String> getFlagValue(@NonNull String alias) {
    return this.getFlag(alias).flatMap(StarboxFlag::getValue);
  }

  /**
   * Get the command that is going to be executed using this context.
   *
   * @return the command
   */
  @NonNull
  T getCommand();

  /**
   * Get if the command was executed with a flag using the given alias.
   *
   * @param alias the alias to check
   * @return true if the command was executed with the flag
   */
  default boolean hasFlag(@NonNull String alias) {
    return this.getCommandLineParser().getFlags().stream().anyMatch(flag -> flag.hasAlias(alias));
  }

  /**
   * Get the sender of the command.
   *
   * @return the sender of the command
   */
  @NonNull
  Object getSender();

  /**
   * Get the providers' registry used in this context. This allows to get the arguments of the
   * command.
   *
   * @return the providers registry
   */
  @NonNull
  ProvidersRegistry<C> getProvidersRegistry();

  /**
   * Get the command line parser used in this context.
   *
   * @return the command line parser
   */
  @NonNull
  CommandLineParser getCommandLineParser();

  /**
   * Get the messages' provider used in this context.
   *
   * @return the messages' provider used in this context
   */
  @NonNull
  MessagesProvider<C> getMessagesProvider();

  /**
   * Get this context to execute a child command.
   *
   * @param subcommand the child command to execute
   * @return the context to execute the child command
   */
  @NonNull
  C getChildren(@NonNull T subcommand);
}
