package com.github.chevyself.starbox.jda;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.CommandManagerBuilder;
import com.github.chevyself.starbox.adapters.Adapter;
import com.github.chevyself.starbox.commands.CommandBuilder;
import com.github.chevyself.starbox.jda.commands.JdaBuiltCommand;
import com.github.chevyself.starbox.jda.commands.JdaCommand;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.listener.CommandListener;
import com.github.chevyself.starbox.jda.messages.GenericJdaMessagesProvider;
import com.github.chevyself.starbox.jda.messages.JdaMessagesProvider;
import com.github.chevyself.starbox.jda.middleware.JdaResultHandlingMiddleware;
import com.github.chevyself.starbox.jda.middleware.PermissionMiddleware;
import com.github.chevyself.starbox.jda.parsers.JdaCommandMetadataParser;
import com.github.chevyself.starbox.jda.parsers.JdaCommandParser;
import com.github.chevyself.starbox.jda.providers.CommandContextProvider;
import com.github.chevyself.starbox.jda.providers.GuildCommandContextProvider;
import com.github.chevyself.starbox.jda.providers.GuildProvider;
import com.github.chevyself.starbox.jda.providers.MemberProvider;
import com.github.chevyself.starbox.jda.providers.MessageProvider;
import com.github.chevyself.starbox.jda.providers.RoleProvider;
import com.github.chevyself.starbox.jda.providers.TextChannelProvider;
import com.github.chevyself.starbox.jda.providers.UserProvider;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandMetadataParser;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.registry.MiddlewareRegistry;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

/** Adapter for the JDA platform. */
public class JdaAdapter implements Adapter<CommandContext, JdaCommand> {

  @NonNull @Getter private final JDA jda;
  @NonNull @Getter private final ListenerOptions listenerOptions;
  @NonNull @Getter private final Map<Long, Set<JdaCommand>> guildCommands;
  private CommandManager<CommandContext, JdaCommand> commandManager;
  private CommandListener listener;

  /**
   * Create the adapter.
   *
   * @param jda the JDA instance
   * @param listenerOptions the listener options
   */
  public JdaAdapter(@NonNull JDA jda, @NonNull ListenerOptions listenerOptions) {
    this.jda = jda;
    this.listenerOptions = listenerOptions;
    this.guildCommands = new ConcurrentHashMap<>();
  }

  /**
   * Get a command by its alias.
   *
   * @param guild the guild to get the command from
   * @param alias the alias of the command
   * @return the command
   */
  @NonNull
  public Optional<JdaCommand> getCommand(Guild guild, @NonNull String alias) {
    if (guild != null) {
      Set<JdaCommand> commands = this.guildCommands.get(guild.getIdLong());
      if (commands != null) {
        Optional<JdaCommand> optional =
            commands.stream().filter(command -> command.getAliases().contains(alias)).findFirst();
        if (optional.isPresent()) {
          return optional;
        }
        return this.getCommand(alias);
      }
    }
    return this.getCommand(alias);
  }

  @NonNull
  private Optional<JdaCommand> getCommand(String alias) {
    return this.commandManager.getCommand(alias);
  }

  @Override
  public void onRegister(@NonNull JdaCommand command) {
    this.jda
        .upsertCommand(command.toCommandData())
        .queue(created -> command.setId(created.getId()));
  }

  @Override
  public void onUnregister(@NonNull JdaCommand command) {
    command.getId().ifPresent(id -> this.jda.deleteCommandById(id).queue());
    command.setId(null);
  }

  @Override
  public void close() {
    jda.removeEventListener(listener);
  }

  @Override
  public void registerDefaultProviders(
      @NonNull CommandManagerBuilder<CommandContext, JdaCommand> builder,
      @NonNull ProvidersRegistry<CommandContext> registry) {
    MessagesProvider<CommandContext> messagesProvider = builder.getMessagesProvider();
    if (messagesProvider instanceof JdaMessagesProvider) {
      JdaMessagesProvider messages = (JdaMessagesProvider) messagesProvider;
      registry
          .addProvider(new GuildCommandContextProvider(messages))
          .addProvider(new GuildProvider(messages))
          .addProvider(new MemberProvider(messages))
          .addProvider(new MessageProvider(messages))
          .addProvider(new RoleProvider(messages))
          .addProvider(new TextChannelProvider(messages))
          .addProvider(new UserProvider(messages));
    } else {
      System.out.println(
          "Failed to add some providers as the messages provider from the builder is not a JdaMessagesProvider");
    }
    registry.addProvider(new CommandContextProvider());
  }

  @Override
  public void registerDefaultMiddlewares(
      @NonNull CommandManagerBuilder<CommandContext, JdaCommand> builder,
      @NonNull MiddlewareRegistry<CommandContext> middlewares) {
    MessagesProvider<CommandContext> messagesProvider = builder.getMessagesProvider();
    if (messagesProvider instanceof JdaMessagesProvider) {
      JdaMessagesProvider jdaMessagesProvider = (JdaMessagesProvider) messagesProvider;
      middlewares.addGlobalMiddleware(new PermissionMiddleware(jdaMessagesProvider));
    } else {
      System.out.println(
          "Failed to add some middlewares as the messages provider from the builder is not a JdaMessagesProvider");
    }
    middlewares.addGlobalMiddleware(new JdaResultHandlingMiddleware());
  }

  @Override
  public void onBuilt(@NonNull CommandManager<CommandContext, JdaCommand> built) {
    this.commandManager = built;
    MessagesProvider<CommandContext> messagesProvider = built.getMessagesProvider();
    this.listener =
        new CommandListener(
            built,
            messagesProvider instanceof JdaMessagesProvider
                ? (JdaMessagesProvider) messagesProvider
                : new GenericJdaMessagesProvider(),
            this,
            this.listenerOptions);
    this.jda.addEventListener(this.listener);
  }

  @Override
  public @NonNull CommandParser<CommandContext, JdaCommand> createParser(
      @NonNull CommandManager<CommandContext, JdaCommand> commandManager) {
    return new JdaCommandParser(this, commandManager);
  }

  @Override
  public @NonNull CommandMetadataParser getDefaultCommandMetadataParser() {
    return new JdaCommandMetadataParser();
  }

  @Override
  public @NonNull MessagesProvider<CommandContext> getDefaultMessagesProvider() {
    return new GenericJdaMessagesProvider();
  }

  @Override
  public @NonNull JdaCommand adapt(@NonNull CommandBuilder<CommandContext, JdaCommand> builder) {
    return new JdaBuiltCommand(builder);
  }
}
