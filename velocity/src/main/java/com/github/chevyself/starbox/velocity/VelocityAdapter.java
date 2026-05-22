package com.github.chevyself.starbox.velocity;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.CommandManagerBuilder;
import com.github.chevyself.starbox.adapters.Adapter;
import com.github.chevyself.starbox.commands.CommandBuilder;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandMetadataParser;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.parsers.EmptyCommandMetadataParser;
import com.github.chevyself.starbox.registry.MiddlewareRegistry;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import com.github.chevyself.starbox.velocity.commands.VelocityBuiltCommand;
import com.github.chevyself.starbox.velocity.commands.VelocityCommand;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import com.github.chevyself.starbox.velocity.messages.GenericVelocityMessagesProvider;
import com.github.chevyself.starbox.velocity.messages.VelocityMessagesProvider;
import com.github.chevyself.starbox.velocity.middleware.PermissionMiddleware;
import com.github.chevyself.starbox.velocity.middleware.VelocityResultHandlingMiddleware;
import com.github.chevyself.starbox.velocity.providers.PlayerProvider;
import com.github.chevyself.starbox.velocity.providers.VelocityCommandContextProvider;
import com.velocitypowered.api.proxy.ProxyServer;
import java.util.logging.Logger;
import lombok.NonNull;

/** The adapter for the Velocity platform. */
public class VelocityAdapter implements Adapter<CommandContext, VelocityCommand> {

  @NonNull private final ProxyServer proxyServer;
  @NonNull private final Logger logger;

  /**
   * Create the adapter.
   *
   * @param proxyServer the proxy server
   * @param logger the logger
   */
  public VelocityAdapter(@NonNull ProxyServer proxyServer, @NonNull Logger logger) {
    this.proxyServer = proxyServer;
    this.logger = logger;
  }

  @Override
  public void onRegister(@NonNull VelocityCommand command) {
    proxyServer
        .getCommandManager()
        .register(
            command.getName(), command.getExecutor(), command.getAliases().toArray(new String[0]));
  }

  @Override
  public void onUnregister(@NonNull VelocityCommand command) {
    proxyServer.getCommandManager().unregister(command.getName());
  }

  @Override
  public void close() {}

  @Override
  public void registerDefaultProviders(
      @NonNull CommandManagerBuilder<CommandContext, VelocityCommand> builder,
      @NonNull ProvidersRegistry<CommandContext> registry) {
    MessagesProvider<CommandContext> messagesProvider = builder.getMessagesProvider();
    if (messagesProvider instanceof VelocityMessagesProvider) {
      VelocityMessagesProvider provider = (VelocityMessagesProvider) messagesProvider;
      registry.addProvider(new PlayerProvider(provider, this.proxyServer));
    } else {
      this.logger.severe(
          "Failed to register some providers, as the MessagesProvider is not a VelocityMessagesProvider");
    }
    registry.addProvider(new VelocityCommandContextProvider());
  }

  @Override
  public void registerDefaultMiddlewares(
      @NonNull CommandManagerBuilder<CommandContext, VelocityCommand> builder,
      @NonNull MiddlewareRegistry<CommandContext> middlewares) {
    MessagesProvider<CommandContext> messagesProvider = builder.getMessagesProvider();
    if (messagesProvider instanceof VelocityMessagesProvider) {
      VelocityMessagesProvider messages = (VelocityMessagesProvider) messagesProvider;
      middlewares.addGlobalMiddleware(new PermissionMiddleware(messages));
    } else {
      this.logger.severe(
          "Failed to register some middlewares, as the MessagesProvider is not a VelocityMessagesProvider");
    }
    middlewares.addGlobalMiddleware(new VelocityResultHandlingMiddleware());
  }

  @Override
  public void onBuilt(@NonNull CommandManager<CommandContext, VelocityCommand> built) {}

  @Override
  public @NonNull CommandParser<CommandContext, VelocityCommand> createParser(
      @NonNull CommandManager<CommandContext, VelocityCommand> commandManager) {
    return new VelocityCommandParser(this, commandManager);
  }

  @Override
  public @NonNull CommandMetadataParser getDefaultCommandMetadataParser() {
    return new EmptyCommandMetadataParser();
  }

  @Override
  public @NonNull MessagesProvider<CommandContext> getDefaultMessagesProvider() {
    return new GenericVelocityMessagesProvider();
  }

  @Override
  public @NonNull VelocityCommand adapt(
      @NonNull CommandBuilder<CommandContext, VelocityCommand> builder) {
    return new VelocityBuiltCommand(builder);
  }
}
