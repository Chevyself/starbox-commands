package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.metadata.CommandMetadata;
import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import com.github.chevyself.starbox.result.Result;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;

/**
 * This class represents a Command which may be executed by a user depending on the implementations
 * it may change.
 *
 * <p>This allows to have children commands which will be recognized using the first parameter of
 * the command as follows:
 *
 * <p>[prefix][parent] [children].
 *
 * <p>This is to give many outputs using a single command
 *
 * @param <C> the context that is required to run the command
 * @param <T> the type of commands that are allowed as children commands
 */
public interface StarboxCommand<
    C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  /**
   * Runs only the command. This is the actual implementation of the logic of the command
   *
   * @param context the context that is required to run the command
   * @return the result of the command execution
   */
  Result run(@NonNull C context);

  /**
   * Executes the command. This will run all middlewares of the command.
   *
   * @param context the context that is required to execute the command
   * @return the result of the command execution
   */
  default Result execute(@NonNull C context) {
    List<String> arguments = context.getCommandLineParser().getArguments();
    if (!arguments.isEmpty()) {
      Optional<T> optionalCommand = this.getChild(arguments.get(0));
      if (optionalCommand.isPresent()) {
        T subcommand = optionalCommand.get();
        return subcommand.execute(context.getChildren(subcommand));
      }
    }
    Result result =
        this.getMiddlewares().stream()
            .map(middleware -> middleware.next(context))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .orElseGet(() -> this.run(context));
    this.getMiddlewares().forEach(middleware -> middleware.next(context, result));
    return result;
  }

  /**
   * Check if the command can the command be recognized by the given alias. This is used because
   * commands have names and aliases, instead of asking for the name and aliases of the command just
   * check if the command has the alias
   *
   * @param alias the alias to check
   * @return true if this command has the given alias
   */
  boolean hasAlias(@NonNull String alias);

  /**
   * Get the name of the command. This will get the first alias of the command.
   *
   * @return the name of the command
   */
  @NonNull
  default String getName() {
    return this.getAliases().get(0);
  }

  /**
   * Get a children command by an alias.
   *
   * @param alias the alias to match the command
   * @return a {@link Optional} instance wrapping the nullable children
   * @see StarboxCommand#hasAlias(String)
   */
  @NonNull
  default Optional<T> getChild(@NonNull String alias) {
    return this.getChildren().stream().filter(child -> child.hasAlias(alias)).findFirst();
  }

  /**
   * Add a children that can be used to run in this.
   *
   * @param command the child command to add
   * @return this same command instance to allow chain methods
   */
  @NonNull
  default StarboxCommand<C, T> addChild(@NonNull T command) {
    this.getChildren().add(command);
    return this;
  }

  /**
   * Add many children that can be used to run in this.
   *
   * @param commands the children command to add
   * @return this same command instance to allow chain methods
   */
  @SuppressWarnings("unchecked")
  @NonNull
  default StarboxCommand<C, T> addChildren(@NonNull T... commands) {
    for (T command : commands) {
      this.addChild(command);
    }
    return this;
  }

  /**
   * Parse the commands from an object and add them as children.
   *
   * @see com.github.chevyself.starbox.parsers.CommandParser#parseAllCommandsFrom(Object)
   * @param object the object to parse the commands from
   * @return this same command instance to allow chain methods
   */
  @NonNull
  default StarboxCommand<C, T> parseAndAddChildren(@NonNull Object object) {
    return this.addChildren(
        this.getCommandManager().getCommandParser().parseAllCommandsFrom(object));
  }

  /**
   * Parse the commands from an array of objects and add them as children.
   *
   * @see com.github.chevyself.starbox.parsers.CommandParser#parseAllCommandsFrom(Object)
   * @param objects the objects to parse the commands from
   * @return this same command instance to allow chain methods
   */
  @NonNull
  default StarboxCommand<C, T> parseAndAddChildren(@NonNull Object... objects) {
    for (Object object : objects) {
      this.parseAndAddChildren(object);
    }
    return this;
  }

  /**
   * Add many children that can be used to run in this.
   *
   * @param commands the children command to add
   * @return this same command instance to allow chain methods
   */
  @NonNull
  default StarboxCommand<C, T> addChildren(@NonNull Collection<T> commands) {
    commands.forEach(this::addChild);
    return this;
  }

  /**
   * Get the Middlewares that will run before the command.
   *
   * @return the collection of middlewares
   */
  @NonNull
  Collection<? extends Middleware<C>> getMiddlewares();

  /**
   * Get the options which this command may have.
   *
   * @return the collection of options
   * @see Option
   */
  @NonNull
  Collection<? extends Option> getOptions();

  /**
   * Get a specific option based on its alias. This will go through all the {@link Option} in {@link
   * #getOptions()} filtering using {@link Option#hasAlias(String)} to find the first match.
   *
   * @param alias the alias to match
   * @return an optional {@link Option}
   */
  @NonNull
  default Optional<? extends Option> getOption(@NonNull String alias) {
    return this.getOptions().stream().filter(option -> option.hasAlias(alias)).findFirst();
  }

  /**
   * Get the collection of registered children in this parent. All the children added in this
   * collection add from {@link #addChild(StarboxCommand)}
   *
   * @return the collection of children
   */
  @NonNull
  Collection<T> getChildren();

  /**
   * Get the names of the children of this command.
   *
   * @return the list of names of the children
   */
  @NonNull
  default List<String> getChildrenNames() {
    return this.getChildren().stream().map(StarboxCommand::getName).collect(Collectors.toList());
  }

  /**
   * Get the command manager that is running this command.
   *
   * @return the command manager
   */
  @NonNull
  CommandManager<C, T> getCommandManager();

  /**
   * Get the providers registry for this command.
   *
   * @return the providers registry
   */
  @NonNull
  ProvidersRegistry<C> getProvidersRegistry();

  /**
   * Get the messages provider for this command.
   *
   * @return the messages provider
   */
  @NonNull
  MessagesProvider<C> getMessagesProvider();

  /**
   * Get the aliases of the command. This is used to recognize the command.
   *
   * @return the list of aliases
   */
  @NonNull
  List<String> getAliases();

  /**
   * Get the metadata of the command.
   *
   * @return the metadata
   */
  @NonNull
  CommandMetadata getMetadata();
}
