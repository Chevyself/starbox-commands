package me.googas.tests;

import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import java.util.Arrays;
import java.util.List;

public class FlagArgumentTest {

  public static void main(String[] args) {
    String command =
        "/command <arg1> -f <arg2> --message=\"\\\"Hello world\\\"\"   \"    This   is also a     n argument\" other   -message = \"Hello world\" -foo=bar -ba = \"Java 1.8\" -foo=\"Hello guys,                                                 Stimpy here\"";
    List<Option> options =
        Arrays.asList(
            Option.create("Whether to force", false, "f", "force"),
            Option.create("The message", true, "m", "message"),
            Option.create("The bar", true, "bar", "b", "ba"),
            Option.create("The foo", true, "foo"));
    CommandLineParser parse = CommandLineParser.parse(options, command);
    parse.getArguments().forEach(System.out::println);
    parse.getFlags().forEach(flag -> System.out.println(flag.getValue().orElse("")));
  }
}
