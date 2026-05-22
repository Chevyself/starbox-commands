package com.github.chevyself.starbox.providers.type;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.providers.StarboxArgumentProvider;
import lombok.NonNull;

/**
 * Provides commands with {@link String}.
 *
 * @param <T> the context
 */
public final class StringProvider<T extends StarboxCommandContext<T, ?>>
    implements StarboxArgumentProvider<String, T> {

  @Override
  public @NonNull Class<String> getClazz() {
    return String.class;
  }

  @NonNull
  @Override
  public String fromString(@NonNull String string, @NonNull T context) {
    return string;
  }
}
