package com.github.chevyself.starbox.bungee.result;

import com.github.chevyself.starbox.bungee.BungeeCommand;
import com.github.chevyself.starbox.result.StarboxResult;
import java.util.List;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * This is the extension for {@link StarboxResult} to be used in the execution of {@link
 * BungeeCommand}.
 *
 * <p>Exceptions will show a simple {@link BungeeResult} message and the stack trace will be
 * printed.
 */
public interface BungeeResult extends StarboxResult {
  /**
   * Get the components to be sent to the {@link net.md_5.bungee.api.CommandSender}.
   *
   * @return the list of components
   */
  @NonNull
  List<BaseComponent> getComponents();
}
