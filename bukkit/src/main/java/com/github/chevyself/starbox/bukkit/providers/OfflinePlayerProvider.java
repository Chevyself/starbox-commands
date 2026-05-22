package com.github.chevyself.starbox.bukkit.providers;

import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.providers.type.BukkitArgumentProvider;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Provides the {@link com.github.chevyself.starbox.CommandManager} with the object {@link
 * OfflinePlayer}.
 */
public class OfflinePlayerProvider implements BukkitArgumentProvider<OfflinePlayer> {

  @Override
  public @NonNull Class<OfflinePlayer> getClazz() {
    return OfflinePlayer.class;
  }

  @NonNull
  @Override
  public OfflinePlayer fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
      if (player.getName() != null && player.getName().equalsIgnoreCase(string)) {
        return player;
      }
    }
    return context.fromString(string, Player.class, context);
  }

  @Override
  public @NonNull List<String> getSuggestions(
      @NonNull String string, @NonNull CommandContext context) {
    List<String> suggestions = new ArrayList<>();
    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
      if (player.getName() != null) {
        suggestions.add(player.getName());
      }
    }
    return suggestions;
  }
}
