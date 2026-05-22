package me.googas;

import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.annotations.Free;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.arguments.ArgumentBehaviour;
import com.github.chevyself.starbox.flags.Flag;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.result.type.SimpleResult;
import com.github.chevyself.starbox.system.context.CommandContext;
import java.time.Duration;
import java.util.Locale;

@SuppressWarnings("JavaDoc")
@Command(
    aliases = "test",
    flags = {@Flag(aliases = "message", value = "Hello world!")})
public class Commands {

  @Command(
      aliases = "a",
      flags = {@Flag(aliases = "message", value = "Hello world!")})
  public Result a(
      CommandContext context,
      @Required(name = "b", behaviour = ArgumentBehaviour.CONTINUOUS) String b,
      @Required(name = "c", behaviour = ArgumentBehaviour.CONTINUOUS) String c,
      @Required(name = "d") int number,
      @Required(name = "e") int number2,
      @Free(behaviour = ArgumentBehaviour.CONTINUOUS) String extra) {
    return new SimpleResult(
        "b = "
            + b
            + ", c = "
            + c
            + ", d = "
            + number
            + ", e = "
            + number2
            + ", message = "
            + context.getFlagValue("message")
            + ", "
            + extra);
  }

  @Command(
      aliases = "message",
      flags = {
        @Flag(
            aliases = {
              "capitalize",
              "c",
            },
            value = "false",
            valuable = false)
      })
  public Result message(
      CommandContext context, @Free(behaviour = ArgumentBehaviour.CONTINUOUS) String message) {
    String string = message == null || message.isEmpty() ? "No message was sent" : message;
    return new SimpleResult(
        context.hasFlag("capitalize") ? string.toUpperCase(Locale.ROOT) : string);
  }

  @Command(aliases = "hello", flags = @Flag(aliases = "message", value = "World!"))
  public Result hello(CommandContext context) {
    return new SimpleResult(context.getFlagValue("message").orElse("IDK :("));
  }

  @Command(aliases = "trollface")
  public Result trollface(
      @Required(name = "message") String message, @Required(name = "number") int number) {
    for (int i = 0; i < number; i++) {
      System.out.println(message);
    }
    return new SimpleCooldownResult("Trolled", Duration.ofSeconds(10));
  }
}
