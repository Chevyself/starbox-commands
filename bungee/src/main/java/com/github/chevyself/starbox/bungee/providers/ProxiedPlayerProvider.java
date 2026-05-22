package com.github.chevyself.starbox.bungee.providers;

import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.bungee.messages.BungeeMessagesProvider;
import com.github.chevyself.starbox.bungee.providers.type.BungeeArgumentProvider;
import com.github.chevyself.starbox.bungee.providers.type.BungeeExtraArgumentProvider;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/** Provides a proxied player. */
public class ProxiedPlayerProvider
    implements BungeeArgumentProvider<ProxiedPlayer>, BungeeExtraArgumentProvider<ProxiedPlayer> {

  @NonNull private final BungeeMessagesProvider messagesProvider;

  /**
   * Create the provider.
   *
   * @param messagesProvider the message provider for the error
   */
  public ProxiedPlayerProvider(@NonNull BungeeMessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Class<ProxiedPlayer> getClazz() {
    return ProxiedPlayer.class;
  }

  @NonNull
  @Override
  public ProxiedPlayer fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(string);
    if (player != null) {
      return player;
    }
    throw new ArgumentProviderException(this.messagesProvider.invalidPlayer(string, context));
  }

  @Override
  public @NonNull ProxiedPlayer getObject(@NonNull CommandContext context)
      throws ArgumentProviderException {
    if (context.getSender() instanceof ProxiedPlayer) {
      return (ProxiedPlayer) context.getSender();
    } else {
      throw new ArgumentProviderException(this.messagesProvider.onlyPlayers(context));
    }
  }

  @Override
  public @NonNull List<String> getSuggestions(
      @NonNull String string, @NonNull CommandContext context) {
    List<String> names = new ArrayList<>();
    for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
      names.add(player.getName());
    }
    return names;
  }
}
