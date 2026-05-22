package com.github.chevyself.starbox.jda.providers;

import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.context.GuildCommandContext;
import com.github.chevyself.starbox.jda.messages.JdaMessagesProvider;
import com.github.chevyself.starbox.jda.providers.type.JdaExtraArgumentProvider;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Guild;

/** Provides the {@link com.github.chevyself.starbox.CommandManager} with a {@link Guild}. */
public class GuildProvider implements JdaExtraArgumentProvider<Guild> {

  private final JdaMessagesProvider messagesProvider;

  /**
   * Create an instance.
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public GuildProvider(JdaMessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NonNull
  @Override
  public Guild getObject(@NonNull CommandContext context) throws ArgumentProviderException {
    if (!(context instanceof GuildCommandContext)) {
      throw new ArgumentProviderException(this.messagesProvider.guildOnly(context));
    }
    return ((GuildCommandContext) context).getGuild();
  }

  @Override
  public @NonNull Class<Guild> getClazz() {
    return Guild.class;
  }
}
