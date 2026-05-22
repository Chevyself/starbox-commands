package com.github.chevyself.starbox.middleware;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.result.type.InternalExceptionResult;
import com.github.chevyself.starbox.result.type.SimpleResult;
import lombok.NonNull;

/**
 * This middleware handles the display of a {@link Result}.
 *
 * @param <C> the context of the command
 */
public abstract class ResultHandlingMiddleware<C extends StarboxCommandContext<C, ?>>
    implements Middleware<C> {

  @Override
  public void next(@NonNull C context, Result result) {
    if (result instanceof SimpleResult) {
      if (result instanceof InternalExceptionResult) {
        ((InternalExceptionResult) result).getException().printStackTrace();
      }
      this.onSimple(context, (SimpleResult) result);
    }
  }

  /**
   * Display a simple result.
   *
   * @param context the context that ran the command
   * @param result the result to display
   */
  public abstract void onSimple(@NonNull C context, @NonNull SimpleResult result);
}
