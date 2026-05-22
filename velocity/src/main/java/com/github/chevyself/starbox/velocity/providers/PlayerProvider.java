package com.github.chevyself.starbox.velocity.providers;

import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import com.github.chevyself.starbox.velocity.messages.VelocityMessagesProvider;
import com.github.chevyself.starbox.velocity.providers.type.VelocityArgumentProvider;
import com.github.chevyself.starbox.velocity.providers.type.VelocityExtraArgumentProvider;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;

/** Provides a {@link Player} from a {@link CommandContext} or {@link String}. */
public class PlayerProvider
    implements VelocityExtraArgumentProvider<Player>, VelocityArgumentProvider<Player> {

  @NonNull private final VelocityMessagesProvider messagesProvider;
  @NonNull private final ProxyServer proxyServer;

  /**
   * Create the provider.
   *
   * @param messagesProvider to message if the {@link String} is invalid
   * @param proxyServer to get the {@link Player} from the {@link String}
   */
  public PlayerProvider(
      @NonNull VelocityMessagesProvider messagesProvider, @NonNull ProxyServer proxyServer) {
    this.messagesProvider = messagesProvider;
    this.proxyServer = proxyServer;
  }

  @Override
  public @NonNull Class<Player> getClazz() {
    return Player.class;
  }

  @Override
  public @NonNull Player getObject(@NonNull CommandContext context)
      throws ArgumentProviderException {
    if (context.getSender() instanceof Player) {
      return (Player) context.getSender();
    } else {
      throw new ArgumentProviderException(this.messagesProvider.onlyPlayers(context));
    }
  }

  @Override
  public @NonNull List<String> getSuggestions(
      @NonNull String string, @NonNull CommandContext context) {
    return proxyServer.getAllPlayers().stream()
        .map(Player::getUsername)
        .collect(Collectors.toList());
  }

  @Override
  public @NonNull Player fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    return proxyServer
        .getPlayer(string)
        .orElseThrow(
            () ->
                new ArgumentProviderException(
                    this.messagesProvider.invalidPlayer(string, context)));
  }
}
