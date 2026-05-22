package com.github.chevyself.starbox.providers.type;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.providers.StarboxArgumentProvider;
import lombok.NonNull;

/**
 * Provides commands with {@link Integer}.
 *
 * @param <C> the context
 */
public final class IntegerProvider<C extends StarboxCommandContext<C, ?>>
    extends MessagedProvider<C> implements StarboxArgumentProvider<Integer, C> {

  /**
   * Create an instance.
   *
   * @param messagesProvider to message if the {@link String} is invalid
   */
  public IntegerProvider(MessagesProvider<C> messagesProvider) {
    super(messagesProvider);
  }

  @Override
  public @NonNull Class<Integer> getClazz() {
    return Integer.class;
  }

  @NonNull
  @Override
  public Integer fromString(@NonNull String string, @NonNull C context)
      throws ArgumentProviderException {
    try {
      return Integer.parseInt(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException(this.messagesProvider.invalidInteger(string, context));
    }
  }

  /**
   * Get if the provider provides with the queried class.
   *
   * @param clazz the queried class
   * @return true if it provides it
   */
  @Override
  public boolean provides(@NonNull Class<?> clazz) {
    return clazz == Integer.class || clazz == int.class;
  }
}
