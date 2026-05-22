package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.metadata.CommandMetadata;
import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

/**
 * Abstract implementation for {@link StarboxCommand}.
 *
 * @param <C> the type of context
 * @param <T> the type of children
 */
@Getter
public abstract class AbstractCommand<
        C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>>
    implements StarboxCommand<C, T> {

  @NonNull protected final List<String> aliases;
  @NonNull protected final List<Middleware<C>> middlewares;
  @NonNull protected final List<Option> options;
  @NonNull protected final CommandMetadata metadata;
  @NonNull protected final List<T> children;
  @NonNull protected final ProvidersRegistry<C> providersRegistry;
  @NonNull protected final MessagesProvider<C> messagesProvider;
  @NonNull private final CommandManager<C, T> commandManager;

  /**
   * Create a new command.
   *
   * @param commandManager the command manager
   * @param aliases the aliases fo the command
   * @param middlewares the middlewares
   * @param options the options
   * @param metadata the metadata
   * @param children the children
   */
  protected AbstractCommand(
      @NonNull CommandManager<C, T> commandManager,
      @NonNull List<String> aliases,
      @NonNull List<Middleware<C>> middlewares,
      @NonNull List<Option> options,
      @NonNull CommandMetadata metadata,
      @NonNull List<T> children) {
    this.commandManager = commandManager;
    this.aliases = aliases;
    this.middlewares = middlewares;
    this.options = options;
    this.metadata = metadata;
    this.children = children;
    this.providersRegistry = commandManager.getProvidersRegistry();
    this.messagesProvider = commandManager.getMessagesProvider();
  }

  /**
   * Create a new abstract command.
   *
   * @param commandManager the command manager
   * @param annotation the annotation
   * @param metadata the metadata
   */
  protected AbstractCommand(
      @NonNull CommandManager<C, T> commandManager,
      @NonNull Command annotation,
      @NonNull CommandMetadata metadata) {
    this(
        commandManager,
        Arrays.asList(annotation.aliases()),
        commandManager.getMiddlewareRegistry().getMiddlewares(annotation),
        Option.of(annotation.flags()),
        metadata,
        new ArrayList<>());
  }

  @Override
  public boolean hasAlias(@NonNull String alias) {
    for (String commandAlias : aliases) {
      if (commandAlias.equalsIgnoreCase(alias)) {
        return true;
      }
    }
    return false;
  }
}
