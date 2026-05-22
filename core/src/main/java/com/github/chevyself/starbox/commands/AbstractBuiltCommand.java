package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.arguments.ArgumentsMap;
import com.github.chevyself.starbox.arguments.SingleArgument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.exceptions.MissingArgumentException;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.result.type.ArgumentExceptionResult;
import com.github.chevyself.starbox.util.Pair;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a command built using {@link CommandBuilder}. This abstract class may be extended by
 * each module to provide a custom implementation of {@link StarboxCommand}.
 *
 * @param <C> the type of command context
 * @param <T> the type of commands
 */
public abstract class AbstractBuiltCommand<
        C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>>
    extends AbstractCommand<C, T> implements ArgumentedStarboxCommand<C, T> {

  @NonNull private final BiFunction<C, ArgumentsMap, Result> executor;
  @NonNull @Getter private final List<Argument<?>> arguments;

  /**
   * Create a new built command.
   *
   * @param builder the builder which built this command
   */
  public AbstractBuiltCommand(@NonNull CommandBuilder<C, T> builder) {
    super(
        builder.getCommandManager(),
        builder.getAliases(),
        builder
            .getCommandManager()
            .getMiddlewareRegistry()
            .getMiddlewares(builder.getExclude(), builder.getInclude()),
        builder.getOptions(),
        builder.getMetadata(),
        builder.getChildren());
    this.executor = builder.getExecutor();
    this.arguments = builder.getArguments();
  }

  @Override
  public Result run(@NonNull C context) {
    try {
      return this.executor.apply(context, AbstractBuiltCommand.getMap(context, this));
    } catch (MissingArgumentException | ArgumentProviderException e) {
      return new ArgumentExceptionResult(e);
    }
  }

  /**
   * Get the {@link ArgumentsMap} from the {@link StarboxCommandContext} at a command execution.
   * This will get all the arguments from {@link ArgumentedStarboxCommand#getArguments()} and put
   * them in a map, according to the input of the user.
   *
   * <p>For example, when registering a command using the builder:
   *
   * <pre>{@code
   * CommandBuilder<CommandContext, ?> builder = ...;
   * builder.argument(new ArgumentBuilder<>(String.class).setName("name"));
   * builder.argument(new ArgumentBuilder<>(Integer.class).setName("age"));
   *
   * }</pre>
   *
   * Then, when executing the command, the user input could be: '/command Jhon 18'. And could be
   * obtained as:
   *
   * <pre>{@code
   *  builder.executes((context, arguments) -> {
   *    String name = arguments.get("name");
   *    int age = arguments.get("age");
   *    return ...;
   * });
   *
   * }</pre>
   *
   * @param context the command context created by the command execution
   * @param command the command which is being executed
   * @return the arguments map
   * @param <C> the type of command context
   * @param <T> the type of commands
   * @throws MissingArgumentException if the user input is missing an argument
   * @throws ArgumentProviderException if the user input is invalid to provide an argument
   */
  @NonNull
  public static <C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>>
      ArgumentsMap getMap(@NonNull C context, @NonNull ArgumentedStarboxCommand<C, T> command)
          throws MissingArgumentException, ArgumentProviderException {
    List<Argument<?>> arguments = command.getArguments();
    Map<String, Object> map = new LinkedHashMap<>(arguments.size());
    List<Object> extra = new ArrayList<>();
    for (Argument<?> argument : arguments) {
      Pair<Object, Integer> pair =
          argument.process(
              command.getProvidersRegistry(), command.getMessagesProvider(), context, 0);
      if (argument instanceof SingleArgument) {
        SingleArgument<?> singleArgument = (SingleArgument<?>) argument;
        map.put(singleArgument.getName(), pair.getA());
      } else {
        extra.add(pair.getA());
      }
    }
    return new ArgumentsMap(map, extra);
  }
}
