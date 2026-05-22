package me.googas;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.CommandManagerBuilder;
import com.github.chevyself.starbox.system.SystemAdapter;
import com.github.chevyself.starbox.system.commands.SystemCommand;
import com.github.chevyself.starbox.system.context.CommandContext;

@SuppressWarnings("JavaDoc")
public class QuickTest {

  public static void main(String[] args) {
    CommandManager<CommandContext, SystemCommand> manager =
        new CommandManagerBuilder<>(new SystemAdapter(args[0])).build();
    manager.parseAndRegisterAll(new Commands());
  }
}
