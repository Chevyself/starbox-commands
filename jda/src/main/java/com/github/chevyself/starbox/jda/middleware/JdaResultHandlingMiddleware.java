package com.github.chevyself.starbox.jda.middleware;

import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.context.SlashCommandContext;
import com.github.chevyself.starbox.middleware.ResultHandlingMiddleware;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.result.type.SimpleResult;
import java.util.Optional;
import lombok.NonNull;

/** Default result handling middleware for JDA. */
public class JdaResultHandlingMiddleware extends ResultHandlingMiddleware<CommandContext> {

  @Override
  public @NonNull Optional<Result> next(@NonNull CommandContext context) {
    if (context instanceof SlashCommandContext) {
      ((SlashCommandContext) context).getEvent().deferReply(true);
    }
    return Optional.empty();
  }

  /**
   * Send a message to the channel based on the context.
   *
   * @param context the context of the command execution
   * @param message the message to send
   */
  public void sendMessage(@NonNull CommandContext context, @NonNull String message) {
    if (context instanceof SlashCommandContext) {
      ((SlashCommandContext) context).getEvent().getHook().sendMessage(message).queue();
    } else {
      context.getChannel().ifPresent(channel -> channel.sendMessage(message).queue());
    }
  }

  @Override
  public void onSimple(@NonNull CommandContext context, @NonNull SimpleResult result) {
    this.sendMessage(context, result.getMessage());
  }
}
