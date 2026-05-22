package com.github.chevyself.starbox.bukkit.middleware;

import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.messages.BukkitMessagesProvider;
import com.github.chevyself.starbox.common.Components;
import com.github.chevyself.starbox.result.Result;
import java.util.Optional;
import lombok.NonNull;

/**
 * This checks the permission of the sender before executing a command. Checked using {@link
 * org.bukkit.command.CommandSender#hasPermission(String)}
 */
public class PermissionMiddleware implements BukkitMiddleware {

  @NonNull private final BukkitMessagesProvider messagesProvider;

  /**
   * Create the middleware.
   *
   * @param messagesProvider the messages provider
   */
  public PermissionMiddleware(@NonNull BukkitMessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Optional<Result> next(@NonNull CommandContext context) {
    final String permission = context.getCommand().getPermission();
    Result result = null;
    if (permission != null && !permission.isEmpty()) {
      if (!context.getSender().hasPermission(permission)) {
        result = Components.asResult(messagesProvider.notAllowed(context));
      }
    }
    return Optional.ofNullable(result);
  }
}
