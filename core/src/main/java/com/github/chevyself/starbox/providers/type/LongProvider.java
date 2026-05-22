package com.github.chevyself.starbox.providers.type;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.providers.StarboxArgumentProvider;
import lombok.NonNull;

/**
 * Provides commands with {@link Long}.
 *
 * @param <C> the context
 */
public final class LongProvider<C extends StarboxCommandContext<C, ?>> extends MessagedProvider<C>
    implements StarboxArgumentProvider<Long, C> {

  /**
   * Create an instance.
   *
   * @param messagesProvider to message if the {@link String} is invalid
   */
  public LongProvider(MessagesProvider<C> messagesProvider) {
    super(messagesProvider);
  }

  @Override
  public @NonNull Class<Long> getClazz() {
    return Long.class;
  }

  @NonNull
  @Override
  public Long fromString(@NonNull String string, @NonNull C context)
      throws ArgumentProviderException {
    try {
      return Long.parseLong(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException(this.messagesProvider.invalidLong(string, context));
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
    return clazz == Long.class || clazz == long.class;
  }
}
