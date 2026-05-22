package com.github.chevyself.starbox.velocity.messages;

import com.github.chevyself.starbox.common.DecoratedMessagesProvider;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import lombok.NonNull;

/** Extends {@link DecoratedMessagesProvider} to provide generic messages for Velocity. */
public class GenericVelocityMessagesProvider extends DecoratedMessagesProvider<CommandContext>
    implements VelocityMessagesProvider {

  @NonNull
  @Override
  public String notAllowed(@NonNull CommandContext context) {
    return String.format(
        "%s%sYou are not allowed to use this command", this.errorPrefix(), this.text());
  }

  @Override
  public @NonNull String onlyPlayers(@NonNull CommandContext context) {
    return String.format("%s%sOnly players can use this command", this.errorPrefix(), this.text());
  }

  @Override
  public @NonNull String invalidPlayer(@NonNull String string, @NonNull CommandContext context) {
    return String.format(
        "%s%s%s %sis not a valid player", this.errorPrefix(), this.element(), string, this.text());
  }
}
