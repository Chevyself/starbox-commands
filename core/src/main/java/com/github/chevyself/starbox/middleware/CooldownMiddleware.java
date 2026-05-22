package com.github.chevyself.starbox.middleware;

import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.result.CooldownResult;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.result.type.SimpleResult;
import java.lang.ref.WeakReference;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;

/**
 * Implementation for middleware that checks for cooldowns.
 *
 * @param <C> the context of the command
 */
public class CooldownMiddleware<C extends StarboxCommandContext<C, ?>> implements Middleware<C> {

  @NonNull private final MessagesProvider<C> messagesProvider;

  @NonNull
  private final Map<WeakReference<StarboxCommand<?, ?>>, Map<WeakReference<?>, LocalDateTime>>
      cooldownMap;

  /**
   * Create the middleware.
   *
   * @param messagesProvider the messages provider to get the cooldown message
   */
  public CooldownMiddleware(@NonNull MessagesProvider<C> messagesProvider) {
    this.messagesProvider = messagesProvider;
    this.cooldownMap = new HashMap<>();
  }

  @Override
  public @NonNull Optional<Result> next(@NonNull C context) {
    Result result = null;
    Optional<Map<WeakReference<?>, LocalDateTime>> commandMap = this.getCommandMap(context);
    if (commandMap.isPresent()) {
      Object sender = context.getSender();
      Map<WeakReference<?>, LocalDateTime> map = commandMap.get();
      Optional<LocalDateTime> cooldown = this.getCooldown(sender, map);
      if (cooldown.isPresent() && cooldown.get().isAfter(LocalDateTime.now())) {
        result =
            new SimpleResult(
                messagesProvider.cooldown(
                    context, Duration.between(LocalDateTime.now(), cooldown.get())));
      } else {
        this.remove(map, sender);
      }
    }
    return Optional.ofNullable(result);
  }

  /**
   * Adds cooldown if the command returns a {@link CooldownResult}.
   *
   * @param context the context that ran the command
   * @param result result returned by the command
   */
  @Override
  public void next(@NonNull C context, Result result) {
    if (result instanceof CooldownResult) {
      Duration duration = ((CooldownResult) result).getCooldown();
      if (!duration.isZero()) {
        this.getCommandMapOrCreate(context)
            .put(new WeakReference<>(context.getSender()), LocalDateTime.now().plus(duration));
      }
    }
  }

  @NonNull
  private Map<WeakReference<?>, LocalDateTime> getCommandMapOrCreate(C context) {
    return this.getCommandMap(context)
        .orElseGet(
            () -> {
              StarboxCommand<?, ?> command = context.getCommand();
              Map<WeakReference<?>, LocalDateTime> map = new HashMap<>();
              cooldownMap.put(new WeakReference<>(command), map);
              return map;
            });
  }

  private void remove(Map<WeakReference<?>, LocalDateTime> map, Object sender) {
    map.entrySet().removeIf(entry -> sender.equals(entry.getKey().get()));
  }

  @NonNull
  private Optional<LocalDateTime> getCooldown(
      @NonNull Object sender, @NonNull Map<WeakReference<?>, LocalDateTime> map) {
    return map.entrySet().stream()
        .filter(entry -> sender.equals(entry.getKey().get()))
        .map(Map.Entry::getValue)
        .findFirst();
  }

  @NonNull
  private Optional<Map<WeakReference<?>, LocalDateTime>> getCommandMap(@NonNull C context) {
    return cooldownMap.entrySet().stream()
        .filter(entry -> context.getCommand().equals(entry.getKey().get()))
        .map(Map.Entry::getValue)
        .findFirst();
  }
}
