package com.github.chevyself.starbox.jda.providers;

import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.messages.JdaMessagesProvider;
import com.github.chevyself.starbox.jda.providers.type.JdaExtraArgumentProvider;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Message;

/** Provides the command with the message in which it was executed. */
public class MessageProvider implements JdaExtraArgumentProvider<Message> {

  private final JdaMessagesProvider messagesProvider;

  /**
   * Create an instance.
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public MessageProvider(JdaMessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NonNull
  @Override
  public Message getObject(@NonNull CommandContext context) throws ArgumentProviderException {
    if (context.getMessage().isPresent()) {
      return context.getMessage().get();
    }
    throw new ArgumentProviderException(messagesProvider.noMessage(context));
  }

  @Override
  public @NonNull Class<Message> getClazz() {
    return Message.class;
  }
}
