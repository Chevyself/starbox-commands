package com.github.chevyself.starbox.providers.type;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.providers.StarboxArgumentProvider;
import lombok.NonNull;

/**
 * Provides commands with {@link Double}.
 *
 * @param <C> the context
 */
public final class DoubleProvider<C extends StarboxCommandContext<C, ?>> extends MessagedProvider<C>
    implements StarboxArgumentProvider<Double, C> {

  /**
   * Create an instance.
   *
   * @param messagesProvider to message if the {@link String} is invalid
   */
  public DoubleProvider(MessagesProvider<C> messagesProvider) {
    super(messagesProvider);
  }

  @Override
  public @NonNull Class<Double> getClazz() {
    return Double.class;
  }

  @NonNull
  @Override
  public Double fromString(@NonNull String string, @NonNull C context)
      throws ArgumentProviderException {
    try {
      return Double.parseDouble(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException(this.messagesProvider.invalidDouble(string, context));
    }
  }

  @Override
  public boolean provides(@NonNull Class<?> clazz) {
    return clazz == Double.class || clazz == double.class;
  }
}
