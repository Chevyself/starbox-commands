package com.github.chevyself.starbox.bukkit.middleware;

import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.result.BukkitResult;
import com.github.chevyself.starbox.bukkit.result.Result;
import java.util.Optional;
import lombok.NonNull;

/**
 * This checks the permission of the sender before executing a command. Checked using {@link
 * org.bukkit.command.CommandSender#hasPermission(String)}
 */
public class PermissionMiddleware implements BukkitMiddleware {

  @Override
  public @NonNull Optional<BukkitResult> next(@NonNull CommandContext context) {
    final String permission = context.getCommand().getPermission();
    if (permission != null && !permission.isEmpty()) {
      if (!context.getSender().hasPermission(permission)) {
        return Optional.of(Result.of(context.getMessagesProvider().notAllowed(context)));
      }
    }
    return Optional.empty();
  }
}
