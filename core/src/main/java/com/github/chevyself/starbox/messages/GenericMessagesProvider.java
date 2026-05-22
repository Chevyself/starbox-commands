package com.github.chevyself.starbox.messages;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import lombok.NonNull;

/** The default {@link MessagesProvider} for commands. */
public class GenericMessagesProvider<C extends StarboxCommandContext<C, ?>>
    extends FormattedMessagesProvider<C> {

  @Override
  public @NonNull String element() {
    return "";
  }

  @Override
  public @NonNull String text() {
    return "";
  }
}
