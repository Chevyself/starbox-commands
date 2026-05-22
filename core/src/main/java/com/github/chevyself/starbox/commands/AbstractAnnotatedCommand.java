package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import java.lang.reflect.Method;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

/**
 * Abstract implementation for {@link ReflectCommand}.
 *
 * @param <C> the context
 * @param <T> the command
 */
@Getter
public abstract class AbstractAnnotatedCommand<
        C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>>
    extends AbstractCommand<C, T> implements ReflectCommand<C, T> {

  @NonNull protected final Object object;
  @NonNull protected final Method method;
  @NonNull protected final List<Argument<?>> arguments;

  /**
   * Create a new annotated command.
   *
   * @param commandManager the command manager
   * @param annotation the annotation
   * @param object the object to invoke
   * @param method the method to invoke
   */
  protected AbstractAnnotatedCommand(
      @NonNull CommandManager<C, T> commandManager,
      @NonNull Command annotation,
      @NonNull Object object,
      @NonNull Method method) {
    super(commandManager, annotation, commandManager.getCommandMetadataParser().parse(method));
    this.object = object;
    this.arguments = Argument.parseArguments(method);
    this.method = method;
  }
}
