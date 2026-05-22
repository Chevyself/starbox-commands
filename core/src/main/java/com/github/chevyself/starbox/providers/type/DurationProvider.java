package com.github.chevyself.starbox.providers.type;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.providers.StarboxArgumentProvider;
import com.github.chevyself.starbox.util.time.TimeUtil;
import java.time.Duration;
import lombok.NonNull;

/**
 * Provides commands with {@link Duration}.
 *
 * @param <C> the context
 */
public class DurationProvider<C extends StarboxCommandContext<C, ?>> extends MessagedProvider<C>
    implements StarboxArgumentProvider<Duration, C> {

  /**
   * Create an instance.
   *
   * @param messagesProvider to message if the {@link String} is invalid
   */
  public DurationProvider(MessagesProvider<C> messagesProvider) {
    super(messagesProvider);
  }

  @Override
  public @NonNull Duration fromString(@NonNull String string, @NonNull C context)
      throws ArgumentProviderException {
    try {
      return TimeUtil.durationOf(string);
    } catch (IllegalArgumentException e) {
      throw new ArgumentProviderException(this.messagesProvider.invalidDuration(string, context));
    }
  }

  @Override
  public @NonNull Class<Duration> getClazz() {
    return Duration.class;
  }
}
