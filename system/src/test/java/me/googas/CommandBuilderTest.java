package me.googas;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.CommandManagerBuilder;
import com.github.chevyself.starbox.arguments.ArgumentBuilder;
import com.github.chevyself.starbox.system.SystemAdapter;
import com.github.chevyself.starbox.system.commands.SystemCommand;
import com.github.chevyself.starbox.system.context.CommandContext;

public class CommandBuilderTest {

  public static void main(String[] args) {
    CommandManager<CommandContext, SystemCommand> commandManager =
        new CommandManagerBuilder<>(new SystemAdapter("")).build();
    commandManager
        .literal("test")
        .executes(
            (context, arguments) -> {
              CommandContext sender = arguments.get(CommandContext.class);
              System.out.println("Sent by " + sender);
              return null;
            })
        .extra(CommandContext.class)
        .argument(new ArgumentBuilder<>(String.class).setName("value").setRequired(true))
        .register();
  }
}
