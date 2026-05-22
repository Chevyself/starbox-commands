package com.github.chevyself.starbox.bungee.middleware;

import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.bungee.messages.BungeeMessagesProvider;
import com.github.chevyself.starbox.common.Components;
import com.github.chevyself.starbox.result.Result;
import java.util.Optional;
import lombok.NonNull;

/** Permission middleware. This checks the permission of the sender before executing a command */
public class PermissionMiddleware implements BungeeMiddleware {

  @NonNull private final BungeeMessagesProvider messagesProvider;

  /**
   * Create the middleware.
   *
   * @param messagesProvider the messages provider
   */
  public PermissionMiddleware(@NonNull BungeeMessagesProvider messagesProvider) {
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
