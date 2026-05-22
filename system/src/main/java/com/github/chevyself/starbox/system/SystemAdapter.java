package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.CommandManagerBuilder;
import com.github.chevyself.starbox.adapters.Adapter;
import com.github.chevyself.starbox.commands.CommandBuilder;
import com.github.chevyself.starbox.messages.GenericMessagesProvider;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.middleware.CooldownMiddleware;
import com.github.chevyself.starbox.parsers.CommandMetadataParser;
import com.github.chevyself.starbox.parsers.EmptyCommandMetadataParser;
import com.github.chevyself.starbox.registry.MiddlewareRegistry;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import com.github.chevyself.starbox.system.commands.SystemBuiltCommand;
import com.github.chevyself.starbox.system.commands.SystemCommand;
import com.github.chevyself.starbox.system.context.CommandContext;
import com.github.chevyself.starbox.system.middleware.SystemResultHandlingMiddleware;
import com.github.chevyself.starbox.system.providers.SystemCommandContextProvider;
import lombok.Getter;
import lombok.NonNull;

/** The adapter for the System platform. */
@Getter
public class SystemAdapter implements Adapter<CommandContext, SystemCommand> {

  @NonNull private final String prefix;
  private CommandListener listener;

  /**
   * Create the adapter.
   *
   * @param prefix the prefix
   */
  public SystemAdapter(@NonNull String prefix) {
    this.prefix = prefix;
  }

  @Override
  public void onRegister(@NonNull SystemCommand command) {}

  @Override
  public void onUnregister(@NonNull SystemCommand command) {}

  @Override
  public void close() {}

  @Override
  public void registerDefaultProviders(
      @NonNull CommandManagerBuilder<CommandContext, SystemCommand> builder,
      @NonNull ProvidersRegistry<CommandContext> registry) {
    registry.addProvider(new SystemCommandContextProvider());
  }

  @Override
  public void registerDefaultMiddlewares(
      @NonNull CommandManagerBuilder<CommandContext, SystemCommand> builder,
      @NonNull MiddlewareRegistry<CommandContext> middlewares) {
    middlewares.addGlobalMiddleware(new SystemResultHandlingMiddleware());
    middlewares.addGlobalMiddleware(new CooldownMiddleware<>(builder.getMessagesProvider()));
  }

  @Override
  public void onBuilt(@NonNull CommandManager<CommandContext, SystemCommand> built) {
    listener = new CommandListener(built, prefix);
    listener.start();
  }

  @Override
  public @NonNull SystemCommandParser createParser(
      @NonNull CommandManager<CommandContext, SystemCommand> commandManager) {
    return new SystemCommandParser(this, commandManager);
  }

  @Override
  public @NonNull CommandMetadataParser getDefaultCommandMetadataParser() {
    return new EmptyCommandMetadataParser();
  }

  @Override
  public @NonNull MessagesProvider<CommandContext> getDefaultMessagesProvider() {
    return new GenericMessagesProvider<>();
  }

  @Override
  public @NonNull SystemCommand adapt(
      @NonNull CommandBuilder<CommandContext, SystemCommand> builder) {
    return new SystemBuiltCommand(builder);
  }
}
