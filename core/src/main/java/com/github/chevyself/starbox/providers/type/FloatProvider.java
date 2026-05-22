package com.github.chevyself.starbox.providers.type;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.providers.StarboxArgumentProvider;
import lombok.NonNull;

/**
 * Provides commands with {@link Float}.
 *
 * @param <C> the context
 */
public final class FloatProvider<C extends StarboxCommandContext<C, ?>> extends MessagedProvider<C>
    implements StarboxArgumentProvider<Float, C> {

  /**
   * Create an instance.
   *
   * @param messagesProvider to message if the {@link String} is invalid
   */
  public FloatProvider(MessagesProvider<C> messagesProvider) {
    super(messagesProvider);
  }

  @Override
  public @NonNull Class<Float> getClazz() {
    return Float.class;
  }

  @NonNull
  @Override
  public Float fromString(@NonNull String string, @NonNull C context)
      throws ArgumentProviderException {
    try {
      return Float.parseFloat(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException(this.messagesProvider.invalidDouble(string, context));
    }
  }

  @Override
  public boolean provides(@NonNull Class<?> clazz) {
    return clazz == Float.class || clazz == float.class;
  }
}
