package com.github.chevyself.starbox.providers.type;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.providers.StarboxArgumentProvider;
import lombok.NonNull;

/**
 * Provides commands with {@link Boolean}.
 *
 * <p>This parses strings as:
 *
 * <p>True:
 *
 * <ul>
 *   <li>true
 *   <li>1
 * </ul>
 *
 * <p>False:
 *
 * <ul>
 *   <li>false
 *   <li>0
 * </ul>
 *
 * @param <C> the context
 */
public final class BooleanProvider<C extends StarboxCommandContext<C, ?>>
    extends MessagedProvider<C> implements StarboxArgumentProvider<Boolean, C> {

  /**
   * Create an instance.
   *
   * @param messagesProvider to message if the {@link String} is invalid
   */
  public BooleanProvider(MessagesProvider<C> messagesProvider) {
    super(messagesProvider);
  }

  @Override
  public @NonNull Class<Boolean> getClazz() {
    return boolean.class;
  }

  @NonNull
  @Override
  public Boolean fromString(@NonNull String string, @NonNull C context)
      throws ArgumentProviderException {
    boolean result;
    if (string.equalsIgnoreCase("true")) {
      result = true;
    } else if (string.equals("1")) {
      result = true;
    } else if (string.equalsIgnoreCase("false")) {
      result = false;
    } else if (string.equals("0")) {
      result = false;
    } else {
      throw new ArgumentProviderException(this.messagesProvider.invalidBoolean(string, context));
    }
    return result;
  }

  @Override
  public boolean provides(@NonNull Class<?> clazz) {
    return clazz == Boolean.class || clazz == boolean.class;
  }
}
