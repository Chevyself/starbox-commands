package com.starfishst.bungee.providers;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.bungee.messages.MessagesProvider;
import com.starfishst.bungee.providers.type.BungeeArgumentProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/** Provides a proxied player */
public class ProxiedPlayerProvider implements BungeeArgumentProvider<ProxiedPlayer> {

  /** The message provider for the error */
  @NonNull private final MessagesProvider messagesProvider;

  /**
   * Create the provider
   *
   * @param messagesProvider the message provider for the error
   */
  public ProxiedPlayerProvider(@NonNull MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull List<String> getSuggestions(CommandContext context) {
    List<String> names = new ArrayList<>();
    for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
      names.add(player.getName());
    }
    return names;
  }

  @Override
  public @NonNull Class<ProxiedPlayer> getClazz() {
    return ProxiedPlayer.class;
  }

  @NonNull
  @Override
  public ProxiedPlayer fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    return ProxyServer.getInstance().getPlayers().stream()
        .filter(player -> player.getName().equalsIgnoreCase(string))
        .findFirst()
        .orElseThrow(
            () -> new ArgumentProviderException(messagesProvider.invalidPlayer(string, context)));
  }
}
