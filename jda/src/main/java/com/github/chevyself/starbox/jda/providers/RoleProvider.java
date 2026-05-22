package com.github.chevyself.starbox.jda.providers;

import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.messages.JdaMessagesProvider;
import com.github.chevyself.starbox.jda.providers.type.JdaArgumentProvider;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Role;

/** Provides the {@link com.github.chevyself.starbox.CommandManager} with a {@link Role}. */
public class RoleProvider implements JdaArgumentProvider<Role> {

  private final JdaMessagesProvider messagesProvider;

  /**
   * Create an instance.
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public RoleProvider(JdaMessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NonNull
  @Override
  public Role fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    Role role;
    try {
      role = context.getJda().getRoleById(UserProvider.getIdFromMention(string));
    } catch (NumberFormatException ignored) {
      role = null;
    }
    if (role != null) {
      return role;
    }
    throw new ArgumentProviderException(this.messagesProvider.invalidRole(string, context));
  }

  @Override
  public @NonNull Class<Role> getClazz() {
    return Role.class;
  }
}
