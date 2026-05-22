package com.github.chevyself.starbox.jda.providers;

import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.context.GenericCommandContext;
import com.github.chevyself.starbox.jda.providers.type.JdaExtraArgumentProvider;
import lombok.NonNull;

/**
 * Provides the {@link com.github.chevyself.starbox.CommandManager} with a {@link
 * GenericCommandContext}.
 */
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
