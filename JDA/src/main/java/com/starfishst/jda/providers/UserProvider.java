package com.starfishst.jda.providers;

import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.messages.MessagesProvider;
import com.starfishst.jda.providers.type.JdaArgumentProvider;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.User;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link User} */
public class UserProvider implements JdaArgumentProvider<User> {

  /** The provider to give the error message */
  private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public UserProvider(MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Class<User> getClazz() {
    return User.class;
  }

  @NonNull
  @Override
  public User fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    for (User user : context.getMessage().getMentionedUsers()) {
      if (string.contains(user.getId())) {
        return user;
      }
    }
    throw new ArgumentProviderException(messagesProvider.invalidUser(string, context));
  }
}
