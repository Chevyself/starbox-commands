package com.starfishst.bungee.providers;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.bungee.providers.type.BungeeExtraArgumentProvider;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;

/** Provides the command manager the sender of the command */
public class CommandSenderProvider implements BungeeExtraArgumentProvider<CommandSender> {

  @NonNull
  @Override
  public CommandSender getObject(@NonNull CommandContext context) {
    return context.getSender();
  }

  @Override
  public @NonNull Class<CommandSender> getClazz() {
    return CommandSender.class;
  }
}
