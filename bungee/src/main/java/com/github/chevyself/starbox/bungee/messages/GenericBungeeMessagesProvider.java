package com.github.chevyself.starbox.bungee.messages;

import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.common.DecoratedMessagesProvider;
import lombok.NonNull;

/** The default messages provider for bungee. */
public class GenericBungeeMessagesProvider extends DecoratedMessagesProvider<CommandContext>
    implements BungeeMessagesProvider {

  @NonNull
  @Override
  public String notAllowed(@NonNull CommandContext context) {
    return String.format(
        "%s%sYou are not allowed to use this command", this.errorPrefix(), this.text());
  }

  @Override
  public @NonNull String invalidPlayer(@NonNull String string, @NonNull CommandContext context) {
    return String.format(
        "%s%s%s %sis not a valid player", this.errorPrefix(), this.element(), string, this.text());
  }

  @Override
  public @NonNull String onlyPlayers(CommandContext context) {
    return String.format("%s%sOnly players can use this command", this.errorPrefix(), this.text());
  }
}
