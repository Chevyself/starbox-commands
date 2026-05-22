package com.github.chevyself.starbox.jda.messages;

import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.messages.GenericMessagesProvider;
import com.github.chevyself.starbox.util.time.TimeUtil;
import java.time.Duration;
import lombok.NonNull;

/**
 * This is a default {@link JdaMessagesProvider} to use if you don't want to create one of your own.
 */
public class GenericJdaMessagesProvider extends GenericMessagesProvider<CommandContext>
    implements JdaMessagesProvider {

  @Override
  public @NonNull String commandNotFound(@NonNull String command, @NonNull CommandContext context) {
    return "Command not found";
  }

  @Override
  public @NonNull String notAllowed(@NonNull CommandContext context) {
    return "You are not allowed to use this command";
  }

  @Override
  public @NonNull String guildOnly(@NonNull CommandContext context) {
    return "You may use this command in a guild";
  }

  @Override
  public @NonNull String cooldown(@NonNull CommandContext context, @NonNull Duration timeLeft) {
    return "You are on cooldown! please wait " + TimeUtil.toString(timeLeft);
  }

  @Override
  public @NonNull String invalidUser(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not a valid user";
  }

  @NonNull
  @Override
  public String invalidMember(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not a valid member";
  }

  @NonNull
  @Override
  public String invalidRole(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not a valid role";
  }

  @NonNull
  @Override
  public String invalidTextChannel(String string, CommandContext context) {
    return string + " is not a valid text channel";
  }

  @Override
  public String noMessage(@NonNull CommandContext context) {
    return "This command must be executed from a message";
  }
}
