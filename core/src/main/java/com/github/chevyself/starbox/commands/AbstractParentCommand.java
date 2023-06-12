package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.metadata.CommandMetadata;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.result.type.SimpleResult;
import lombok.NonNull;

public abstract class AbstractParentCommand<
        C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>>
    extends AbstractCommand<C, T> {

  public AbstractParentCommand(
      @NonNull CommandManager<C, T> commandManager, @NonNull Command annotation) {
    super(commandManager, annotation, new CommandMetadata());
  }

  @Override
  public Result run(@NonNull C context) {
    return new SimpleResult(StarboxCommand.genericHelp(this, this.getChild()));
  }
}
