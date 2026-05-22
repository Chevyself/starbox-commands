package com.github.chevyself.starbox.jda;

import com.github.chevyself.starbox.jda.listener.CommandListener;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Guild;

/**
 * The listener options allow to change the output of the {@link CommandListener} to Discord.
 *
 * <p>For example:
 *
 * <ul>
 *   <li>Deleting the usage command
 *   <li>Deleting the result command
 *   <li>Embedding messages
 * </ul>
 */
public interface ListenerOptions {

  /**
   * Get the prefix that is used for commands inside a {@link Guild}. If the parameter {@link Guild}
   * is null means that the command was not executed inside of one meaning that the message was sent
   * in private messages
   *
   * @param guild the guild where the command was executed or null if it was not executed in one
   * @return the prefix that runs the commands inside the guild
   */
  @NonNull
  String getPrefix(Guild guild);
}
