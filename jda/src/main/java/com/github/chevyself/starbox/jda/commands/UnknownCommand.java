package com.github.chevyself.starbox.jda.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.commands.AbstractCommand;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.messages.JdaMessagesProvider;
import com.github.chevyself.starbox.metadata.CommandMetadata;
import com.github.chevyself.starbox.result.Result;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import lombok.NonNull;

/** This represents a command which wasn't found in {@link CommandManager}. */
public class UnknownCommand extends AbstractCommand<CommandContext, JdaCommand>
    implements JdaCommand {

  @NonNull private final JdaMessagesProvider messagesProvider;

  /**
   * Create the unknown command.
   *
   * @param commandManager the command manager
   * @param name the name of the command
   * @param messagesProvider the messages provider
   */
  public UnknownCommand(
      @NonNull CommandManager<CommandContext, JdaCommand> commandManager,
      @NonNull String name,
      @NonNull JdaMessagesProvider messagesProvider) {
    super(
        commandManager,
        Collections.singletonList(name),
        new ArrayList<>(),
        new ArrayList<>(),
        new CommandMetadata(),
        new ArrayList<>());
    this.messagesProvider = messagesProvider;
  }

  @Override
  public Result run(@NonNull CommandContext context) {
    return Result.of(messagesProvider.commandNotFound(this.getName(), context));
  }

  @Override
  public @NonNull String getDescription() {
    return "No description provided";
  }

  @Override
  public @NonNull Optional<String> getId() {
    return Optional.empty();
  }

  @Override
  public void setId(String id) {}
}
