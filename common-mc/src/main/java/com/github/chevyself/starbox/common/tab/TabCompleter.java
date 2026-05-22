package com.github.chevyself.starbox.common.tab;

import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import java.util.List;
import lombok.NonNull;

/**
 * Generates the tab completion for a command in Minecraft platforms.
 *
 * @param <C> the command context
 * @param <T> the command
 * @param <O> the sender
 */
public interface TabCompleter<
    C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>, O> {

  /**
   * Generate the tab completion.
   *
   * @param command the command that is going to be executed
   * @param sender the sender of the command
   * @param name the name of the command
   * @param strings the current arguments of the command execution
   * @return the suggestions for the next argument
   */
  @NonNull
  List<String> tabComplete(
      @NonNull T command, @NonNull O sender, @NonNull String name, @NonNull String[] strings);
}
