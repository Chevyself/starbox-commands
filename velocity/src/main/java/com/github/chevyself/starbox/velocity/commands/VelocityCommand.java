package com.github.chevyself.starbox.velocity.commands;

import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.velocity.VelocityCommandExecutor;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import lombok.NonNull;

/** Extension of the {@link StarboxCommand} for the Velocity platform. */
public interface VelocityCommand extends StarboxCommand<CommandContext, VelocityCommand> {

  /**
   * Get the executor for this command. This is used to register the command with the Velocity API.
   *
   * @return the executor
   */
  @NonNull
  VelocityCommandExecutor getExecutor();

  /**
   * Get the permission to run this command.
   *
   * @return the permission or null if none is required
   */
  String getPermission();

  /**
   * Get whether this command should be executed asynchronously.
   *
   * @return true if the command should be executed asynchronously
   */
  boolean isAsync();
}
