package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.metadata.CommandMetadata;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.result.type.SimpleResult;
import com.github.chevyself.starbox.util.Strings;
import lombok.NonNull;

/**
 * Abstract implementation for parent commands.
 *
 * @param <C> the type of context
 * @param <T> the type of children
 */
public abstract class AbstractParentCommand<
        C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>>
    extends AbstractCommand<C, T> {

  /**
   * Create a new parent command.
   *
   * @param commandManager the command manager
   * @param annotation the annotation
   */
  public AbstractParentCommand(
      @NonNull CommandManager<C, T> commandManager, @NonNull Command annotation) {
    super(commandManager, annotation, new CommandMetadata());
  }

  @Override
  public Result run(@NonNull C context) {
    return new SimpleResult(Strings.genericHelp(this, this.getChildren()));
  }
}
