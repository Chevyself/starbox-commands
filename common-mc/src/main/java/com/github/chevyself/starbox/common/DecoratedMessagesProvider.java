package com.github.chevyself.starbox.common;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.FormattedMessagesProvider;
import java.time.Duration;
import lombok.NonNull;

/**
 * Extension of {@link FormattedMessagesProvider} that adds a prefix to error messages.
 *
 * @param <C> the command context type
 */
public class DecoratedMessagesProvider<C extends StarboxCommandContext<C, ?>>
    extends FormattedMessagesProvider<C> {

  /**
   * Get the prefix that will be added to error messages.
   *
   * @return the prefix
   */
  public @NonNull String errorPrefix() {
    return "&e&o⚠ &r";
  }

  @Override
  public @NonNull String element() {
    return "&4&o";
  }

  @Override
  public @NonNull String text() {
    return "&c&o";
  }

  @Override
  public @NonNull String invalidLong(@NonNull String string, @NonNull C context) {
    return this.errorPrefix() + super.invalidLong(string, context);
  }

  @Override
  public @NonNull String invalidInteger(@NonNull String string, @NonNull C context) {
    return this.errorPrefix() + super.invalidInteger(string, context);
  }

  @Override
  public @NonNull String invalidDouble(@NonNull String string, @NonNull C context) {
    return this.errorPrefix() + super.invalidDouble(string, context);
  }

  @Override
  public @NonNull String invalidBoolean(@NonNull String string, @NonNull C context) {
    return this.errorPrefix() + super.invalidBoolean(string, context);
  }

  @Override
  public @NonNull String invalidDuration(@NonNull String string, @NonNull C context) {
    return this.errorPrefix() + super.invalidDuration(string, context);
  }

  @Override
  public @NonNull String missingArgument(
      @NonNull String name, @NonNull String description, int position, C context) {
    return this.errorPrefix() + super.missingArgument(name, description, position, context);
  }

  @Override
  public @NonNull String cooldown(@NonNull C context, @NonNull Duration timeLeft) {
    return this.errorPrefix() + super.cooldown(context, timeLeft);
  }
}
