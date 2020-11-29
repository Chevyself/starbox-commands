package com.starfishst.jda.providers;

import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.context.GuildCommandContext;
import com.starfishst.jda.messages.MessagesProvider;
import com.starfishst.jda.providers.type.JdaArgumentProvider;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Member;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link Member} */
public class MemberProvider implements JdaArgumentProvider<Member> {

  /** The provider to give the error message */
  private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public MemberProvider(MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NonNull
  @Override
  public Member fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    if (!(context instanceof GuildCommandContext)) {
      throw new ArgumentProviderException(messagesProvider.guildOnly(context));
    } else {
      for (Member member : context.getMessage().getMentionedMembers()) {
        if (string.contains(member.getId())) {
          return member;
        }
      }
      throw new ArgumentProviderException(messagesProvider.invalidMember(string, context));
    }
  }

  @Override
  public @NonNull Class<Member> getClazz() {
    return Member.class;
  }
}
