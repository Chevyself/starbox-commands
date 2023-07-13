package com.github.chevyself.starbox.velocity.middleware;

import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import com.github.chevyself.starbox.velocity.messages.VelocityMessagesProvider;
import java.util.Optional;
import lombok.NonNull;

public class PermissionMiddleware implements Middleware<CommandContext> {

  @NonNull private final VelocityMessagesProvider messagesProvider;

  public PermissionMiddleware(@NonNull VelocityMessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Optional<Result> next(@NonNull CommandContext context) {
    Result result;
    if (context.getCommand().getPermission() != null
        && !context.getSender().hasPermission(context.getCommand().getPermission())) {
      return Optional.of(Result.of(messagesProvider.notAllowed(context)));
    }
    return Optional.empty();
  }
}