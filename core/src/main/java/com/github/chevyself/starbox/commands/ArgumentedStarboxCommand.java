package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.arguments.SingleArgument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;

/**
 * Esteemed extension of {@link StarboxCommand} for commands with arguments.
 *
 * @param <C> the context
 * @param <T> the command
 */
public interface ArgumentedStarboxCommand<
        C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>>
    extends StarboxCommand<C, T> {
  /**
   * Get the {@link List} of the arguments for the command.
   *
   * @return the {@link List} of {@link Argument}
   */
  @NonNull
  List<Argument<?>> getArguments();

  /**
   * Get the argument of certain position. A basic loop checking if the {@link SingleArgument}
   * position matches the queried position. Ignore the extra arguments as those don't have positions
   *
   * @param position the position to get the argument of
   * @return the argument if exists, empty otherwise
   */
  @NonNull
  default Optional<SingleArgument<?>> getArgument(int position) {
    SingleArgument<?> singleArgument = null;
    for (Argument<?> argument : this.getArguments()) {
      if (argument instanceof SingleArgument
          && ((SingleArgument<?>) argument).getPosition() == position) {
        singleArgument = (SingleArgument<?>) argument;
      }
    }
    return Optional.ofNullable(singleArgument);
  }
}
