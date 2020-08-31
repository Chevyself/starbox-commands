package com.starfishst.core;

import com.starfishst.core.context.ICommandContext;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a command. This one contains the names/aliases in a {@link List} because there could
 * be some incompatibilities between frameworks
 *
 * @param <C> the type of command context
 */
public interface ICommand<C extends ICommandContext> extends ISimpleCommand<C> {

  /**
   * Get the aliases of the command. The main name of the command will be in the index 0 Check the
   * respective @Command annotation to know more on how the alias will be gotten
   *
   * @return the {@link List} of aliases
   */
  @NotNull
  List<String> getAliases();
}
