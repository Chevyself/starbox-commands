package com.github.chevyself.starbox;

import com.github.chevyself.starbox.adapters.Adapter;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandMetadataParser;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.registry.MiddlewareRegistry;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import java.util.ArrayList;
import lombok.Getter;
import lombok.NonNull;

/**
 * Create a new CommandManager instance. Use this class to create a new {@link CommandManager}
 * instance. In the constructor a {@link Adapter} is required, you must check the adapter of the
 * platform you are using.
 *
 * <p>You can set a {@link ProvidersRegistry} and a {@link MiddlewareRegistry} using the setters:
 * {@link #setProvidersRegistry(ProvidersRegistry)} and {@link
 * #setMiddlewareRegistry(MiddlewareRegistry)}, you can also set whether to use the default
 * middlewares and providers using {@link #useDefaultMiddlewares(boolean)} and {@link
 * #useDefaultProviders(boolean)}. Default middlewares and providers are given by the adapter:
 * {@link Adapter#registerDefaultMiddlewares(CommandManagerBuilder, MiddlewareRegistry)} and {@link
 * Adapter#registerDefaultProviders(CommandManagerBuilder, ProvidersRegistry)}. The default values
 * will be set even if a new registry is set, therefore you must set the default values to false if
 * you want to not use them.
 *
 * <p>You can set a {@link MessagesProvider} and a {@link CommandMetadataParser} using the setters:
 * {@link #setMessagesProvider(MessagesProvider)} and {@link
 * #setCommandMetadataParser(CommandMetadataParser)}. If none are set, the default ones will be used
 * from the adapter: {@link Adapter#getDefaultMessagesProvider()} and {@link
 * Adapter#getDefaultCommandMetadataParser()}.
 *
 * <p>Once the {@link CommandManager} is built, the setters will throw an {@link
 * IllegalStateException}.
 *
 * @param <C> the type of the command context
 * @param <T> the type of the command
 */
public final class CommandManagerBuilder<
    C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  @NonNull private final Adapter<C, T> adapter;
  @NonNull @Getter private ProvidersRegistry<C> providersRegistry;
  @NonNull @Getter private MiddlewareRegistry<C> middlewareRegistry;
  @Getter private boolean useDefaultMiddlewares;
  @Getter private boolean useDefaultProviders;
  @Getter private MessagesProvider<C> messagesProvider;
  @Getter private CommandMetadataParser commandMetadataParser;
  private String base;
  private CommandManager<C, T> built;

  /**
   * Create a new builder. This will create new registries and both {@link #useDefaultProviders} and
   * {@link #useDefaultMiddlewares} will be set to true.
   *
   * @param adapter the adapter of the platform to use
   */
  public CommandManagerBuilder(@NonNull Adapter<C, T> adapter) {
    this.adapter = adapter;
    this.providersRegistry = new ProvidersRegistry<>();
    this.middlewareRegistry = new MiddlewareRegistry<>();
    this.useDefaultMiddlewares = true;
    this.useDefaultProviders = true;
    this.messagesProvider = null;
    this.commandMetadataParser = null;
    this.base = null;
  }

  /**
   * Set a base package to scan for commands, middlewares and providers. This will register all
   * commands inside the base package and providers and middlewares inside '.providers' and
   * '.middlewares' respectively.
   *
   * @param base the base package to scan
   * @return this builder
   */
  @NonNull
  public CommandManagerBuilder<C, T> setBase(String base) {
    this.base = base;
    return this;
  }

  /**
   * Set a new {@link ProvidersRegistry} to use. This will override the default one.
   *
   * @param providersRegistry the new providers registry
   * @return this builder
   * @throws IllegalStateException if the {@link CommandManager} has already been initialized
   */
  public @NonNull CommandManagerBuilder<C, T> setProvidersRegistry(
      @NonNull ProvidersRegistry<C> providersRegistry) {
    this.checkNotInitialized();
    this.providersRegistry = providersRegistry;
    return this;
  }

  /**
   * Set a new {@link MiddlewareRegistry} to use. This will override the default one.
   *
   * @param middlewareRegistry the new middleware registry
   * @return this builder
   * @throws IllegalStateException if the {@link CommandManager} has already been initialized
   */
  public @NonNull CommandManagerBuilder<C, T> setMiddlewareRegistry(
      @NonNull MiddlewareRegistry<C> middlewareRegistry) {
    this.checkNotInitialized();
    this.middlewareRegistry = middlewareRegistry;
    return this;
  }

  /**
   * Set a new {@link MessagesProvider} to use. This will override the default one.
   *
   * @param messagesProvider the new messages provider
   * @return this builder
   * @throws IllegalStateException if the {@link CommandManager} has already been initialized
   */
  public @NonNull CommandManagerBuilder<C, T> setMessagesProvider(
      @NonNull MessagesProvider<C> messagesProvider) {
    this.checkNotInitialized();
    this.messagesProvider = messagesProvider;
    return this;
  }

  /**
   * Set a new {@link CommandMetadataParser} to use. This will override the default one.
   *
   * @param commandMetadataParser the new command metadata parser
   * @return this builder
   * @throws IllegalStateException if the {@link CommandManager} has already been initialized
   */
  public @NonNull CommandManagerBuilder<C, T> setCommandMetadataParser(
      CommandMetadataParser commandMetadataParser) {
    this.commandMetadataParser = commandMetadataParser;
    return this;
  }

  /**
   * Whether to use the default middlewares or not.
   *
   * @param use whether to use the default middlewares or not
   * @return this builder
   * @throws IllegalStateException if the {@link CommandManager} has already been initialized
   */
  public @NonNull CommandManagerBuilder<C, T> useDefaultMiddlewares(boolean use) {
    this.checkNotInitialized();
    this.useDefaultMiddlewares = use;
    return this;
  }

  /**
   * Whether to use the default providers or not.
   *
   * @param use whether to use the default providers or not
   * @return this builder
   * @throws IllegalStateException if the {@link CommandManager} has already been initialized
   */
  public @NonNull CommandManagerBuilder<C, T> useDefaultProviders(boolean use) {
    this.checkNotInitialized();
    this.useDefaultProviders = use;
    return this;
  }

  /** Checks that {@link #built} is null, if not, throws an {@link IllegalStateException}. */
  private void checkNotInitialized() {
    if (this.built != null) {
      throw new IllegalStateException("CommandManager has already been initialized.");
    }
  }

  /**
   * Build the {@link CommandManager}. <br>
   * The build steps are:
   *
   * <ul>
   *   <li>Set the {@link MessagesProvider} to the default one if none is set
   *   <li>Register the default middlewares if {@link #useDefaultMiddlewares} is true
   *   <li>Register the default providers if {@link #useDefaultProviders} is true
   *   <li>Set the {@link CommandMetadataParser} to the default one if none is set
   *   <li>Build the {@link CommandManager}
   * </ul>
   *
   * @return the built {@link CommandManager}
   */
  @NonNull
  public CommandManager<C, T> build() {
    if (built == null) {
      if (messagesProvider == null) {
        this.messagesProvider = adapter.getDefaultMessagesProvider();
      }
      if (useDefaultMiddlewares) {
        this.adapter.registerDefaultMiddlewares(this, this.middlewareRegistry);
      }
      if (useDefaultProviders) {
        this.providersRegistry.registerDefaults(this.messagesProvider);
        this.adapter.registerDefaultProviders(this, this.providersRegistry);
      }
      if (this.commandMetadataParser == null) {
        this.commandMetadataParser = this.adapter.getDefaultCommandMetadataParser();
      }
      this.built =
          new CommandManager<>(
              this.adapter,
              new ArrayList<>(),
              this.providersRegistry,
              this.middlewareRegistry,
              this.messagesProvider,
              commandMetadataParser);
      this.adapter.onBuilt(this.built);
      this.registerBase();
    }
    return this.built;
  }

  private void registerBase() {
    if (this.base != null) {
      CommandParser<C, T> parser = this.built.getCommandParser();
      parser
          .parseAllMiddlewaresIn(this.base + ".middlewares")
          .forEach(middleware -> this.middlewareRegistry.addMiddleware(middleware));
      parser
          .parseAllProvidersIn(this.base + ".providers")
          .forEach(provider -> this.providersRegistry.addProvider(provider));
      this.built.registerAll(parser.parseAllCommandsIn(this.base));
    }
  }
}
