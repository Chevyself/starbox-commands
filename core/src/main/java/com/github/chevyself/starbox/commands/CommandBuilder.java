package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.arguments.ArgumentBuilder;
import com.github.chevyself.starbox.arguments.ArgumentsMap;
import com.github.chevyself.starbox.arguments.SingleArgument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.metadata.CommandMetadata;
import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.result.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;

/**
 * Builder for commands. This implementation allows to build commands in a fluent way, using chain
 * methods and a builder pattern.
 *
 * <p>This also reduces reflection usage, and allows the creation of commands without the need of a
 * {@link java.lang.reflect.Method} meaning that you could create more dynamic commands
 *
 * @param <C> the type of command context used
 * @param <T> the type of command built
 */
@Getter
public class CommandBuilder<C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  @NonNull private final CommandManager<C, T> commandManager;
  @NonNull private final List<String> aliases;
  @NonNull private final List<Class<? extends Middleware<C>>> include;
  @NonNull private final List<Class<? extends Middleware<C>>> exclude;
  @NonNull private final List<Option> options;
  @NonNull private final List<T> children;
  @NonNull private final List<CommandBuilder<C, T>> extraChildren;
  @NonNull private final List<Argument<?>> arguments;
  @NonNull private final CommandMetadata metadata;
  @NonNull private BiFunction<C, ArgumentsMap, Result> executor;

  /**
   * Create a new command builder.
   *
   * @param commandManager the command manager
   * @param name the name of the command
   */
  public CommandBuilder(@NonNull CommandManager<C, T> commandManager, @NonNull String name) {
    this.commandManager = commandManager;
    this.aliases = new ArrayList<>();
    this.include = new ArrayList<>();
    this.exclude = new ArrayList<>();
    this.options = new ArrayList<>();
    this.children = new ArrayList<>();
    this.extraChildren = new ArrayList<>();
    this.arguments = new ArrayList<>();
    this.metadata = new CommandMetadata();
    this.executor = (context, arguments) -> null;
    this.aliases.add(name);
  }

  /**
   * Set the actual logic of the command.
   *
   * @param executor the new logic
   * @return this builder
   */
  @NonNull
  public CommandBuilder<C, T> executes(@NonNull BiFunction<C, ArgumentsMap, Result> executor) {
    this.executor = executor;
    return this;
  }

  /**
   * Add a new argument to the command.
   *
   * @param argument the argument to add
   * @return this builder
   */
  @NonNull
  public CommandBuilder<C, T> argument(@NonNull Argument<?> argument) {
    this.arguments.add(argument);
    return this;
  }

  /**
   * Build a new argument and add it to the command.
   *
   * @param builder the argument builder
   * @return this builder
   */
  @NonNull
  public CommandBuilder<C, T> argument(@NonNull ArgumentBuilder<?> builder) {
    return this.argument(builder.build(this.nextArgumentIndex()));
  }

  /**
   * Add a new extra argument to the command.
   *
   * @param clazz the class of the extra argument to provide
   * @return this builder
   */
  @NonNull
  public CommandBuilder<C, T> extra(@NonNull Class<?> clazz) {
    return this.argument(new ArgumentBuilder<>(clazz).setExtra(true));
  }

  /**
   * Add a children command in a form of another builder.
   *
   * @param child the child builder
   * @return this builder
   */
  @NonNull
  public CommandBuilder<C, T> child(@NonNull CommandBuilder<C, T> child) {
    this.extraChildren.add(child);
    return this;
  }

  /**
   * Change the metadata of the command.
   *
   * @param consumer the consumer to change the metadata
   * @return this builder
   */
  @NonNull
  public CommandBuilder<C, T> withMetadata(@NonNull Consumer<CommandMetadata> consumer) {
    consumer.accept(this.metadata);
    return this;
  }

  private int nextArgumentIndex() {
    int index = 0;
    for (Argument<?> argument : this.arguments) {
      if (argument instanceof SingleArgument) {
        index++;
      }
    }
    return index;
  }

  /** Register the command. */
  public void register() {
    this.commandManager.register(this.build());
  }

  /**
   * Build the command. This might be useful to use the command as a child command.
   *
   * @return the built command
   */
  @NonNull
  public T build() {
    return this.commandManager.getAdapter().adapt(this);
  }
}
