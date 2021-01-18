package com.starfishst.commands.jda.providers;

import com.starfishst.commands.jda.context.CommandContext;
import com.starfishst.commands.jda.providers.type.JdaExtraArgumentProvider;
import lombok.NonNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link CommandContext} */
public class CommandContextProvider implements JdaExtraArgumentProvider<CommandContext> {

  @NonNull
  @Override
  public CommandContext getObject(@NonNull CommandContext context) {
    return context;
  }

  @Override
  public @NonNull Class<CommandContext> getClazz() {
    return CommandContext.class;
  }
}