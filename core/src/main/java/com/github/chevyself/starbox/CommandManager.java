package com.github.chevyself.starbox;

import com.github.chevyself.starbox.adapters.Adapter;
import com.github.chevyself.starbox.commands.CommandBuilder;
import com.github.chevyself.starbox.commands.ReflectCommand;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandMetadataParser;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.providers.StarboxContextualProvider;
import com.github.chevyself.starbox.registry.MiddlewareRegistry;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents the command manager. This class is the main class of the framework, it contains the
 * registered commands, the registries for {@link StarboxContextualProvider} and {@link
 * com.github.chevyself.starbox.middleware.Middleware}, the {@link MessagesProvider}, the {@link
 * CommandParser} the {@link CommandMetadataParser} and the {@link Adapter}.
 *
 * @param <C> the command context type
 * @param <T> the command type
 */
@Getter
public final class CommandManager<
    C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  @NonNull private final Adapter<C, T> adapter;
  @NonNull private final CommandParser<C, T> commandParser;
  @NonNull private final CommandMetadataParser commandMetadataParser;
  @NonNull private final List<T> commands;
  @NonNull private final ProvidersRegistry<C> providersRegistry;
  @NonNull private final MiddlewareRegistry<C> middlewareRegistry;
  @NonNull private final MessagesProvider<C> messagesProvider;

  CommandManager(
      @NonNull Adapter<C, T> adapter,
      @NonNull List<T> commands,
      @NonNull ProvidersRegistry<C> providersRegistry,
      @NonNull MiddlewareRegistry<C> middlewareRegistry,
      @NonNull MessagesProvider<C> messagesProvider,
      @NonNull CommandMetadataParser metadataParser) {
    this.adapter = adapter;
    this.commandParser = adapter.createParser(this);
    this.commands = commands;
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
    this.middlewareRegistry = middlewareRegistry;
    this.commandMetadataParser = metadataParser;
  }

  /**
   * Register a new command. Any command that implements the type {@link T} can be registered.
   *
   * @param command the command to be registered
   * @return this manager
   */
  @NonNull
  public CommandManager<C, T> register(@NonNull T command) {
    this.commands.add(command);
    this.adapter.onRegister(command);
    return this;
  }

  /**
   * Parses the command from the object and registers it. This will call {@link
   * CommandParser#parseAllCommandsFrom(Object)} and then {@link #registerAll(Collection)}.
   *
   * <p>This method is used to register commands using reflection, with the annotations {@link
   * com.github.chevyself.starbox.annotations.Command} and {@link
   * com.github.chevyself.starbox.annotations.CommandCollection}
   *
   * @param object the object to parse the commands from
   * @return this manager
   */
  @NonNull
  public CommandManager<C, T> parseAndRegister(@NonNull Object object) {
    this.registerAll(this.getCommandParser().parseAllCommandsFrom(object));
    return this;
  }

  /**
   * Parse and register all the commands from the objects in an array. This will loop around each
   * object to execute {@link #parseAndRegister(Object)}
   *
   * @param objects the objects to parse and parseAndRegister commands from
   * @return this manager
   */
  @NonNull
  public CommandManager<C, T> parseAndRegisterAll(@NonNull Object... objects) {
    for (Object object : objects) {
      this.parseAndRegister(object);
    }
    return this;
  }

  /**
   * Registers the collection of commands. This will call {@link #register(StarboxCommand)} on loop
   *
   * @param commands the commands to be registered
   * @return this manager
   */
  @NonNull
  public CommandManager<C, T> registerAll(@NonNull Collection<? extends T> commands) {
    for (T command : commands) {
      this.register(command);
    }
    return this;
  }

  /**
   * Registers a collection of commands. This will call {@link #registerAll(Collection)}
   *
   * @param commands the commands to be registered
   * @return this same command manager instance to allow chain method calls
   * @see #register(StarboxCommand)
   */
  @SafeVarargs
  @NonNull
  public final CommandManager<C, T> registerAll(@NonNull T... commands) {
    return this.registerAll(Arrays.asList(commands));
  }

  /** Closes the command manager. */
  public void close() {
    this.commands.forEach(this.adapter::onUnregister);
    this.commands.clear();
    this.adapter.close();
    this.commandParser.close();
    this.commandMetadataParser.close();
    this.providersRegistry.close();
    this.middlewareRegistry.close();
  }

  /**
   * Registers all the commands in the provided package. This will loop around each class that is
   * annotated with either {@link com.github.chevyself.starbox.annotations.Command} or {@link
   * com.github.chevyself.starbox.annotations.CommandCollection}.
   *
   * @param packageName the package name to get the commands from
   * @return this same instance
   */
  @NonNull
  public CommandManager<C, T> registerAllIn(@NonNull String packageName) {
    return this.registerAll(this.getCommandParser().parseAllCommandsIn(packageName));
  }

  /**
   * Unregisters a command from the manager. This will remove the command from the list and call
   * {@link Adapter#onUnregister(StarboxCommand)}
   *
   * @param command the command to be unregistered
   * @return this manager
   */
  @NonNull
  public CommandManager<C, T> unregister(@NonNull T command) {
    this.commands.remove(command);
    this.adapter.onUnregister(command);
    return this;
  }

  /**
   * Unregisters all the commands in the collection. This will loop around each command to call
   * {@link #unregister(StarboxCommand)}
   *
   * @param commands the commands to be unregistered
   * @return this manager
   */
  @NonNull
  public CommandManager<C, T> unregisterAll(@NonNull T... commands) {
    for (T command : commands) {
      this.unregister(command);
    }
    return this;
  }

  /**
   * Unregisters all the commands in the collection. This will loop around each command to call
   * {@link #unregister(StarboxCommand)}
   *
   * @param commands the commands to be unregistered
   * @return this manager
   */
  @NonNull
  public CommandManager<C, T> unregisterAll(@NonNull Collection<? extends T> commands) {
    commands.forEach(this::unregister);
    return this;
  }

  /**
   * Unregister all the commands that use the provided object to invoke methods. This will filter
   * the commands that are instances of {@link ReflectCommand} and have the same object as the
   * provided one.
   *
   * @param object the object to filter the commands
   * @return this manager
   */
  @NonNull
  public CommandManager<C, T> unregisterAllIn(@NonNull Object object) {
    this.commands.stream()
        .filter(
            command ->
                command instanceof ReflectCommand
                    && ((ReflectCommand<?, ?>) command).getObject().equals(object))
        .forEach(this::unregister);
    return this;
  }

  /**
   * Get a command by its name. This will filter the command which {@link
   * StarboxCommand#hasAlias(String)} returns true.
   *
   * @param alias the alias to get the command from
   * @return the command if found, or an empty optional if not
   */
  @NonNull
  public Optional<T> getCommand(@NonNull String alias) {
    return this.commands.stream().filter(command -> command.hasAlias(alias)).findFirst();
  }

  /**
   * Create a new command builder with the provided name.
   *
   * @param name the name of the command
   * @return the command builder
   */
  @NonNull
  public CommandBuilder<C, T> literal(@NonNull String name) {
    return new CommandBuilder<>(this, name);
  }
}
