package com.github.chevyself.starbox.jda.middleware;

import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.result.Result;
import java.util.Optional;
import lombok.NonNull;

/** This is an implementation of {@link Middleware} for the JDA module. */
public interface JdaMiddleware extends Middleware<CommandContext> {

  @Override
  @NonNull
  Optional<Result> next(@NonNull CommandContext context);
}
