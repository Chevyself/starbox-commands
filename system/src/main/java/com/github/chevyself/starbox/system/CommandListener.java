package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import com.github.chevyself.starbox.system.commands.SystemCommand;
import com.github.chevyself.starbox.system.context.CommandContext;
import com.github.chevyself.starbox.system.context.sender.ConsoleCommandSender;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import lombok.Getter;
import lombok.NonNull;

/**
 * This object is a {@link Thread} with a {@link Scanner} which waits for {@link System#in}. This
 * checks that the line starts with the prefix if it does then it will check that the name matches a
 * {@link SystemCommand} in {@link CommandManager#getCommand(String)} if it does it will execute the
 * command
 */
public class CommandListener extends Thread {

  @NonNull private final Scanner scanner = new Scanner(System.in);
  @NonNull private final CommandManager<CommandContext, SystemCommand> manager;
  @NonNull @Getter private final String prefix;
  private boolean closed = false;

  /**
   * Create the listener.
   *
   * @param manager the command manager to get the commands
   * @param prefix the prefix to differentiate messages from commands
   */
  public CommandListener(
      @NonNull CommandManager<CommandContext, SystemCommand> manager, @NonNull String prefix) {
    this.manager = manager;
    this.prefix = prefix;
  }

  @Override
  public void run() {
    while (!closed) {
      if (this.scanner.hasNextLine()) {
        String line = this.scanner.nextLine().trim();
        this.process(line);
      }
    }
  }

  /**
   * Process a command line.
   *
   * @param line the line to process
   */
  public void process(@NonNull String line) {
    String[] split = line.split(" ");
    String name = split[0];
    if (!name.startsWith(this.prefix)) {
      return;
    }
    Optional<SystemCommand> optionalCommand =
        this.manager.getCommand(name.substring(this.prefix.length()));
    if (optionalCommand.isPresent()) {
      SystemCommand command = optionalCommand.get();
      CommandLineParser parser =
          CommandLineParser.parse(command.getOptions(), Arrays.copyOfRange(split, 1, split.length));
      command.execute(
          new CommandContext(
              parser,
              command,
              ConsoleCommandSender.INSTANCE,
              this.manager.getProvidersRegistry(),
              this.manager.getMessagesProvider()));
    } else {
      System.out.println("Command " + name + " could not be found");
    }
  }

  /** Stops the listener. */
  public void finish() {
    closed = true;
  }
}
