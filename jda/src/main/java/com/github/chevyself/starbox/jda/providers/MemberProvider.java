package com.github.chevyself.starbox.jda.providers;

import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.context.GuildCommandContext;
import com.github.chevyself.starbox.jda.messages.JdaMessagesProvider;
import com.github.chevyself.starbox.jda.providers.type.JdaArgumentProvider;
import com.github.chevyself.starbox.jda.providers.type.JdaExtraArgumentProvider;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

/** Provides the {@link com.github.chevyself.starbox.CommandManager} with a {@link Member}. */
public class MemberProvider
    implements JdaArgumentProvider<Member>, JdaExtraArgumentProvider<Member> {

  private final JdaMessagesProvider messagesProvider;

  /**
   * Create an instance.
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public MemberProvider(JdaMessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NonNull
  @Override
  public Member fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    TextChannel channel = context.getProvidersRegistry().getObject(TextChannel.class, context);
    Guild guild = channel.getGuild();
    Member member =
        guild.getMember(context.getProvidersRegistry().fromString(string, User.class, context));
    if (member != null) {
      return member;
    }
    throw new ArgumentProviderException(messagesProvider.invalidUser(string, context));
  }

  @Override
  public @NonNull Class<Member> getClazz() {
    return Member.class;
  }

  @Override
  public @NonNull Member getObject(@NonNull CommandContext context)
      throws ArgumentProviderException {
    if (!(context instanceof GuildCommandContext)) {
      throw new ArgumentProviderException(this.messagesProvider.guildOnly(context));
    } else {
      return ((GuildCommandContext) context).getMember();
    }
  }
}
