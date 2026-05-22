package com.github.chevyself.starbox.providers.type;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.providers.StarboxExtraArgumentProvider;
import lombok.NonNull;

/**
 * Provides commands with {@link StarboxCommandContext}.
 *
 * @param <C> the context
 */
public abstract class CommandContextProvider<C extends StarboxCommandContext<C, ?>>
    implements StarboxExtraArgumentProvider<C, C> {

  @Override
  public @NonNull C getObject(@NonNull C context) throws ArgumentProviderException {
    return context;
  }
}
