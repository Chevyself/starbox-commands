package com.github.chevyself.starbox.bukkit.middleware;

import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.utils.BukkitUtils;
import com.github.chevyself.starbox.common.ComponentResult;
import com.github.chevyself.starbox.common.Components;
import com.github.chevyself.starbox.middleware.ResultHandlingMiddleware;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.result.type.SimpleResult;
import lombok.NonNull;

/** Handles the result of a command for the bukkit platform. */
public class BukkitResultHandlingMiddleware extends ResultHandlingMiddleware<CommandContext>
    implements BukkitMiddleware {

  @Override
  public void next(@NonNull CommandContext context, Result result) {
    if (result instanceof ComponentResult) {
      BukkitUtils.send(context.getSender(), ((ComponentResult) result).getComponents());
    } else {
      super.next(context, result);
    }
  }

  @Override
  public void onSimple(@NonNull CommandContext context, @NonNull SimpleResult result) {
    BukkitUtils.send(context.getSender(), Components.getComponent(result.getMessage()));
  }
}
