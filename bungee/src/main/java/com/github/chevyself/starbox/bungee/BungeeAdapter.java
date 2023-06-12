package com.github.chevyself.starbox.bungee;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.CommandManagerBuilder;
import com.github.chevyself.starbox.adapters.Adapter;
import com.github.chevyself.starbox.bungee.commands.BungeeCommand;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.bungee.messages.BungeeMessagesProvider;
import com.github.chevyself.starbox.bungee.providers.CommandContextProvider;
import com.github.chevyself.starbox.bungee.providers.CommandSenderProvider;
import com.github.chevyself.starbox.bungee.providers.ProxiedPlayerProvider;
import com.github.chevyself.starbox.messages.GenericMessagesProvider;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandMetadataParser;
import com.github.chevyself.starbox.parsers.EmptyCommandMetadataParser;
import com.github.chevyself.starbox.registry.MiddlewareRegistry;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeAdapter implements Adapter<CommandContext, BungeeCommand> {

  @NonNull @Getter private final Plugin plugin;

  public BungeeAdapter(@NonNull Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public void onRegister(@NonNull BungeeCommand command) {}

  @Override
  public void onUnregister(@NonNull BungeeCommand command) {}

  @Override
  public void close() {}

  @Override
  public void registerDefaultProviders(
      @NonNull CommandManagerBuilder<CommandContext, BungeeCommand> builder,
      @NonNull ProvidersRegistry<CommandContext> registry) {
    MessagesProvider<CommandContext> messagesProvider = builder.getMessagesProvider();
    if (messagesProvider instanceof BungeeMessagesProvider) {
      BungeeMessagesProvider messages = (BungeeMessagesProvider) messagesProvider;
      registry.addProvider(new ProxiedPlayerProvider(messages));
    } else {
      plugin
          .getLogger()
          .severe(
              "Failed to register default providers that require a messages provider as the messages provider that has been set in the builder is not a BungeeMessagesProvider.");
    }
    registry.addProvider(new CommandContextProvider()).addProvider(new CommandSenderProvider());
  }

  @Override
  public void registerDefaultMiddlewares(
      @NonNull CommandManagerBuilder<CommandContext, BungeeCommand> builder,
      @NonNull MiddlewareRegistry<CommandContext> middlewares) {}

  @Override
  public void onBuilt(@NonNull CommandManager<CommandContext, BungeeCommand> built) {}

  @Override
  public @NonNull BungeeCommandParser createParser(
      @NonNull CommandManager<CommandContext, BungeeCommand> commandManager) {
    return new BungeeCommandParser(this, commandManager);
  }

  @Override
  public @NonNull CommandMetadataParser getDefaultCommandMetadataParser() {
    return new EmptyCommandMetadataParser();
  }

  @Override
  public @NonNull MessagesProvider<CommandContext> getDefaultMessaesProvider() {
    return new GenericMessagesProvider<>();
  }
}