package me.googas.testing;

import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.annotations.Free;
import com.github.chevyself.starbox.annotations.Required;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.util.Strings;
import net.dv8tion.jda.api.entities.Member;

public class TestCommands {

  @Command(aliases = "test", description = "This is a test command")
  public Result test(
      @Required(name = "name", description = "Your name") String name,
      @Required(name = "game", description = "Your favourite game") String game,
      @Free(
              name = "age",
              description = "Your age",
              suggestions = {"0", "1"})
          int age) {
    return Result.of(
        Strings.format(
            "Hello {0}! Your favourite game is {1} and you are {2} years old", name, game, age));
  }

  @Command(aliases = "lol", description = "The lol command")
  public Result lol() {
    return Result.of("LOL!");
  }

  @Command(aliases = "foo", description = "Who is a foo")
  public Result foo(@Required(name = "foo", description = "The foo") Member member) {
    return Result.of("The foo is ");
  }
}
