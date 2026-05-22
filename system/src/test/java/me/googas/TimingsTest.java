package me.googas;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.CommandManagerBuilder;
import com.github.chevyself.starbox.result.type.SimpleResult;
import com.github.chevyself.starbox.system.CommandListener;
import com.github.chevyself.starbox.system.SystemAdapter;
import com.github.chevyself.starbox.system.commands.SystemCommand;
import com.github.chevyself.starbox.system.context.CommandContext;
import java.lang.reflect.Field;
import lombok.NonNull;

public class TimingsTest {

  public static void main(String[] args) throws IllegalAccessException {
    // This test has been created to see how much do commands improve from using MethodHandle
    // instead
    // of the reflection instance of Method
    SystemAdapter adapter = new SystemAdapter("");
    CommandManager<CommandContext, SystemCommand> commandManager =
        new CommandManagerBuilder<>(adapter).build();

    // Simple hello world command
    commandManager
        .literal("timings")
        .executes((context, arguments) -> new SimpleResult("Hello world!"))
        .register();

    // Let's run it 1_000_000 times

    CommandListener listener = TimingsTest.getListener(adapter);
    long start = System.currentTimeMillis();
    for (int i = 0; i < 1_000_000; i++) {
      listener.process("timings");
    }
    long end = System.currentTimeMillis();
    System.out.println("Took " + (end - start) + "ms");
  }

  @NonNull
  private static CommandListener getListener(@NonNull SystemAdapter adapter)
      throws IllegalAccessException {
    Field field = adapter.getClass().getDeclaredFields()[1];
    field.setAccessible(true);
    return (CommandListener) field.get(adapter);
  }
}
