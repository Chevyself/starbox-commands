package com.github.chevyself.starbox.system.providers;

import com.github.chevyself.starbox.providers.type.CommandContextProvider;
import com.github.chevyself.starbox.system.context.CommandContext;
import lombok.NonNull;

/** Provides the {@link CommandContext} for the System platform. */
public class SystemCommandContextProvider extends CommandContextProvider<CommandContext> {

  @Override
  public @NonNull Class<CommandContext> getClazz() {
    return CommandContext.class;
  }
}
