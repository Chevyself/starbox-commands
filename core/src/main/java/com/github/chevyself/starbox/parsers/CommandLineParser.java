package com.github.chevyself.starbox.parsers;

import com.github.chevyself.starbox.flags.FlagArgument;
import com.github.chevyself.starbox.flags.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.NonNull;

/**
 * This object contains the parsed elements from the command line. It can be used to get the flags
 * and arguments from the command line.
 */
public final class CommandLineParser {

  /**
   * The pattern to match the flags from the command line. It matches the following:
   *
   * <ul>
   *   <li><code>--flag</code>
   *   <li><code>-flag</code>
   *   <li><code>--flag=value</code>
   *   <li><code>--flag = value</code>
   *   <li><code>-flag="value with spaces"</code>
   *   <li><code>-flag = "value with spaces"</code>
   * </ul>
   *
   * Values can be surrounded by quotation marks to allow spaces.
   */
  @NonNull
  private static final Pattern flagPattern =
      Pattern.compile(
          "\\s*-{1,2}(\\w+)(?:[ \t]*=[ \t]*)?((\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\")|\\S+)?\\s*");

  /** The arguments as they were passed to the command line. */
  @NonNull private final String rawString;
  /** The available options or flags. */
  @NonNull private final List<Option> options;
  // Parsed values
  /** The flags matched from the command line. */
  @NonNull @Getter private final List<FlagArgument> flags;
  /** The arguments matched from the command line. */
  @NonNull @Getter private final List<String> arguments;
  /** The arguments as a single string. */
  private String argumentsString;

  private boolean parsed;

  private CommandLineParser(
      @NonNull String rawString,
      @NonNull Collection<? extends Option> options,
      @NonNull List<FlagArgument> flags,
      @NonNull List<String> arguments,
      String argumentsString,
      boolean parsed) {
    this.rawString = rawString;
    this.options = new ArrayList<>(options);
    this.flags = flags;
    this.arguments = arguments;
    this.argumentsString = argumentsString;
    this.parsed = parsed;
  }

  private CommandLineParser(
      @NonNull String rawString, @NonNull Collection<? extends Option> options) {
    this(rawString, options, new ArrayList<>(), new ArrayList<>(), null, false);
  }

  /**
   * Parse the flags from the command line.
   *
   * @param options the list of options to check the flags
   * @param strings the command line as a single string
   * @return the parser that got the flags
   * @see CommandLineParser#parse
   */
  @NonNull
  public static CommandLineParser parse(
      @NonNull Collection<? extends Option> options, @NonNull String strings) {
    return CommandLineParser.parse(options, strings.split(" "));
  }

  /**
   * Parse the flags from the command line.
   *
   * @param options the list of options to check the flags
   * @param strings the command line as an array of strings
   * @return the parser that got the flags
   * @see CommandLineParser#parse
   */
  @NonNull
  public static CommandLineParser parse(
      @NonNull Collection<? extends Option> options, @NonNull String... strings) {
    return new CommandLineParser(String.join(" ", strings), options).parse();
  }

  private static String getValue(String value, @NonNull Option option) {
    Optional<String> defValue = option.getValue();
    if (option.isValuable() && value == null && defValue.isPresent()) {
      value = defValue.get();
    }
    if (value != null) {
      value = CommandLineParser.validateQuotation(value);
    }
    return value;
  }

  private static @NonNull String validateQuotation(@NonNull String value) {
    // Replaces quotation marks that are not escaped to empty strings and escaped quotation marks
    // to quotation marks, thus: \"hello world\" -> "hello world"
    return value.replaceAll("(?<!\\\\)\"", "").replace("\\\"", "\"");
  }

  private static boolean hasSpaces(@NonNull String string) {
    return !string.equals(" ") && string.contains(" ");
  }

  /**
   * Parse the flags and arguments from the command line.
   *
   * @return the parser but with the flags and arguments parsed
   */
  @NonNull
  private CommandLineParser parse() {
    if (this.parsed) {
      return this;
    }
    String joined = rawString;
    joined = this.parseFlags(joined);
    joined = this.parseArguments(joined);
    this.parsed = true;
    this.argumentsString = joined;
    return this;
  }

  @NonNull
  private String parseArguments(@NonNull String joined) {
    if (!joined.isEmpty()) {
      this.arguments.addAll(Arrays.asList(joined.split(" +")));
    }
    return joined;
  }

  @NonNull
  private String parseFlags(@NonNull String joined) {
    Matcher matcher = CommandLineParser.flagPattern.matcher(joined);
    while (matcher.find() && matcher.groupCount() >= 2) {
      String flag = matcher.group(1);
      String value = matcher.group(2);
      String group = matcher.group();
      Optional<? extends Option> optional = this.getOption(flag);
      if (optional.isPresent()) {
        Option option = optional.get();
        value = CommandLineParser.getValue(value, option);
        // We must have a space as the pattern removes whitespaces completely
        joined = joined.replaceFirst(Pattern.quote(group), " ");
        this.flags.add(new FlagArgument(option, value));
      }
    }
    return joined;
  }

  @NonNull
  private Optional<? extends Option> getOption(@NonNull String alias) {
    return options.stream().filter(options -> options.getAliases().contains(alias)).findFirst();
  }

  /**
   * Get the argument line as a single string.
   *
   * @return the string
   */
  @NonNull
  public String getArgumentsString() {
    return Objects.requireNonNull(this.argumentsString, "The arguments are not parsed yet");
  }

  /**
   * Creates a copy of the parser, but with the arguments from the specified position.
   *
   * @param position the position to start copying
   * @param options the new options to add in the parser
   * @return the new parser
   */
  @NonNull
  public CommandLineParser copyFrom(int position, @NonNull Collection<? extends Option> options) {
    String rawCopy = String.join(" ", this.arguments.subList(position, this.arguments.size()));
    List<Option> optionsCopy = new ArrayList<>(this.options);
    optionsCopy.addAll(options);
    CommandLineParser copy =
        new CommandLineParser(
                rawCopy, optionsCopy, new ArrayList<>(this.flags), new ArrayList<>(), null, false)
            .parse();
    copy.getFlags().addAll(this.flags);
    return copy;
  }

  @Override
  public String toString() {
    return "CommandLineParser{"
        + "rawString='"
        + rawString
        + '\''
        + ", options="
        + options
        + ", flags="
        + flags
        + ", arguments="
        + arguments
        + ", argumentsString='"
        + argumentsString
        + '\''
        + ", parsed="
        + parsed
        + '}';
  }
}
