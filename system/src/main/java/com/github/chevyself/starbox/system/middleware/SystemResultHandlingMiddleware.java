package com.github.chevyself.starbox.system.middleware;

import com.github.chevyself.starbox.middleware.ResultHandlingMiddleware;
import com.github.chevyself.starbox.result.type.SimpleResult;
import com.github.chevyself.starbox.system.context.CommandContext;
import lombok.NonNull;

/** Middleware that handles {@link SimpleResult}s for the System platform. */
public class SystemResultHandlingMiddleware extends ResultHandlingMiddleware<CommandContext>
    implements SystemMiddleware {

  @Override
  public void onSimple(@NonNull CommandContext context, @NonNull SimpleResult result) {
    System.out.println(result.getMessage());
  }
}
