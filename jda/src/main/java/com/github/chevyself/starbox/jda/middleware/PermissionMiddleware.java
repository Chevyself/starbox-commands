package com.github.chevyself.starbox.jda.middleware;

import com.github.chevyself.starbox.jda.commands.JdaCommand;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.messages.JdaMessagesProvider;
import com.github.chevyself.starbox.result.Result;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

/**
 * Permission middleware. This checks the permission of the sender before executing a command.
 *
 * <p>To use this middleware you must use the {@link
 * com.github.chevyself.starbox.jda.annotations.CommandPermission} annotation in the method or class
 * of the command.
 */
public class PermissionMiddleware implements JdaMiddleware {

  @NonNull private final JdaMessagesProvider messagesProvider;
  @NonNull private final Map<WeakReference<JdaCommand>, Permission> cached;

  /**
   * Create the middleware.
   *
   * @param messagesProvider the messages provider
   */
  public PermissionMiddleware(@NonNull JdaMessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
    this.cached = new HashMap<>();
  }

  @Override
  public @NonNull Optional<Result> next(@NonNull CommandContext context) {
    Permission permission = this.getPermission(context);
    Optional<Member> optional =
        context.getMessage().map(message -> message.isFromGuild() ? message.getMember() : null);
    Result result = null;
    if (permission != Permission.UNKNOWN) {
      if (optional.isPresent()) {
        if (!optional.get().hasPermission(permission)) {
          result = Result.of(messagesProvider.notAllowed(context));
        }
      } else {
        result = Result.of(messagesProvider.notAllowed(context));
      }
    }
    return Optional.ofNullable(result);
  }

  private Permission getPermission(@NonNull CommandContext context) {
    return this.getCached(context)
        .orElseGet(
            () -> {
              Object object = context.getCommand().getMetadata().get("permission");
              if (object instanceof Permission) {
                this.cached.put(new WeakReference<>(context.getCommand()), (Permission) object);
                return (Permission) object;
              }
              return null;
            });
  }

  @NonNull
  private Optional<Permission> getCached(@NonNull CommandContext context) {
    return this.cached.entrySet().stream()
        .filter(entry -> context.getCommand().equals(entry.getKey().get()))
        .map(Map.Entry::getValue)
        .findFirst();
  }
}
