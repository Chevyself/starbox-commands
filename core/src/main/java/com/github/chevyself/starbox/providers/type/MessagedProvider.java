package com.github.chevyself.starbox.providers.type;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.MessagesProvider;

/**
 * Allows to have generic messages to be used in the framework.
 *
 * @param <C> the context
 */
public abstract class MessagedProvider<C extends StarboxCommandContext<C, ?>> {

  protected final MessagesProvider<C> messagesProvider;

  /**
   * Create an instance.
   *
   * @param messagesProvider to message if the {@link String} is invalid
   */
  public MessagedProvider(MessagesProvider<C> messagesProvider) {
    this.messagesProvider = messagesProvider;
  }
}
