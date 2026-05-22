package com.github.chevyself.starbox.bungee.middleware;

import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.common.ComponentResult;
import com.github.chevyself.starbox.common.Components;
import com.github.chevyself.starbox.middleware.ResultHandlingMiddleware;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.result.type.SimpleResult;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * This middleware sends the components of the {@link Result} to the {@link
 * net.md_5.bungee.api.CommandSender}.
 */
public class BungeeResultHandlingMiddleware extends ResultHandlingMiddleware<CommandContext>
    implements BungeeMiddleware {

  @Override
  public void next(@NonNull CommandContext context, Result result) {
    if (result instanceof ComponentResult
        && !((ComponentResult) result).getComponents().isEmpty()) {
      context
          .getSender()
          .sendMessage(((ComponentResult) result).getComponents().toArray(new BaseComponent[0]));
    } else {
      super.next(context, result);
    }
  }

  @Override
  public void onSimple(@NonNull CommandContext context, @NonNull SimpleResult result) {
    context.getSender().sendMessage(Components.getComponent(result.getMessage()));
  }
}
