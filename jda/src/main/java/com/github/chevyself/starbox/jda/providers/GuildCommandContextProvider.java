package com.github.chevyself.starbox.jda.providers;

import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.context.GuildCommandContext;
import com.github.chevyself.starbox.jda.messages.JdaMessagesProvider;
import com.github.chevyself.starbox.jda.providers.type.JdaExtraArgumentProvider;
import lombok.NonNull;

/**
 * Provides the {@link com.github.chevyself.starbox.CommandManager} with a {@link
 * GuildCommandContext}.
 */
public class GuildCommandContextProvider implements JdaExtraArgumentProvider<GuildCommandContext> {

  private final JdaMessagesProvider messagesProvider;

  /**
   * Create an instance.
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public GuildCommandContextProvider(JdaMessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NonNull
  @Override
  public GuildCommandContext getObject(@NonNull CommandContext context)
      throws ArgumentProviderException {
    if (!(context instanceof GuildCommandContext)) {
      throw new ArgumentProviderException(this.messagesProvider.guildOnly(context));
    }
    return (GuildCommandContext) context;
  }

  @Override
  public @NonNull Class<GuildCommandContext> getClazz() {
    return GuildCommandContext.class;
  }
}
