package com.github.chevyself.starbox.adapters;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.CommandManagerBuilder;
import com.github.chevyself.starbox.commands.CommandBuilder;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandMetadataParser;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.registry.MiddlewareRegistry;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import lombok.NonNull;

/**
 * Helper class to adapt the command manager to a specific platform. On the basic need of code
 * reusability, this class is used to adapt the command manager to a specific platform. For example,
 * the 'BukkitAdapter' adapts the command manager to the Bukkit API.
 *
 * @param <C> the type of the command context for the platform
 * @param <T> the type of the command for the platform
 */
public interface Adapter<C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  /**
   * Runs after the command is registered in {@link CommandManager#register(StarboxCommand)}.
   *
   * @param command the command that was registered
   */
  void onRegister(@NonNull T command);

  /**
   * Runs after the command is unregistered.
   *
   * @param command the command that was unregistered
   */
  void onUnregister(@NonNull T command);

  /** Close the adapter. This method is called when the command manager is closed. */
  void close();

  /**
   * Register the default providers for the platform.
   *
   * @param builder the command manager builder
   * @param registry the registry to register the providers
   */
  void registerDefaultProviders(
      @NonNull CommandManagerBuilder<C, T> builder, @NonNull ProvidersRegistry<C> registry);

  /**
   * Register the default middlewares for the platform.
   *
   * @param builder the command manager builder
   * @param middlewares the registry to register the middlewares
   */
  void registerDefaultMiddlewares(
      @NonNull CommandManagerBuilder<C, T> builder, @NonNull MiddlewareRegistry<C> middlewares);

  /**
   * Runs after the command manager is built. {@link CommandManager} will be built from: {@link
   * CommandManagerBuilder#build()}
   *
   * @param built the command manager that was built
   */
  void onBuilt(@NonNull CommandManager<C, T> built);

  /**
   * Creates a command parser for a {@link CommandManager}. This method is called when the command
   * manager is being built.
   *
   * @param commandManager the command manager to create the parser for
   * @return the command parser
   */
  @NonNull
  CommandParser<C, T> createParser(@NonNull CommandManager<C, T> commandManager);

  /**
   * Get the default command metadata parser for the platform.
   *
   * @return the default command metadata parser
   */
  @NonNull
  CommandMetadataParser getDefaultCommandMetadataParser();

  /**
   * Get the default messages provider for the platform.
   *
   * @return the default messages provider
   */
  @NonNull
  MessagesProvider<C> getDefaultMessagesProvider();

  /**
   * Adapts a command builder to the platform. This means creating a new command builder that is
   * adapted to the platform.
   *
   * @param builder the command builder to adapt
   * @return the adapted command builder
   */
  @NonNull
  T adapt(@NonNull CommandBuilder<C, T> builder);
}
