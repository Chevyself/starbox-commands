package com.github.chevyself.starbox.util;

import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.commands.ReflectCommand;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.flags.Option;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import lombok.NonNull;

/** Static utilities for {@link String}. */
public final class Strings {

  /** A string only containing uppercase letters. */
  @NonNull public static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  /** A string only containing lowercase letters. */
  @NonNull public static final String LOWERCASE_LETTERS = Strings.UPPERCASE_LETTERS.toLowerCase();
  /** A char array containing both uppercase and lowercase letters. */
  public static final char[] LETTERS =
      (Strings.UPPERCASE_LETTERS + Strings.LOWERCASE_LETTERS).toCharArray();
  /** A char array containing uppercase letters. */
  public static final char[] UPPERCASE = Strings.UPPERCASE_LETTERS.toCharArray();
  /** A char array containing lowercase letters. */
  public static final char[] LOWERCASE = Strings.LOWERCASE_LETTERS.toCharArray();
  /** Null representation. */
  @NonNull public static final String NULL = "null";

  /**
   * This method is made to save resources from {@link #format(String, Map)}, {@link #format(String,
   * Object...)} to not go in a loop. In case that the message is null it will just give a string
   * with the characters "null"
   *
   * @param message the message to format
   * @return {@link #NULL} if the message is null else the message
   */
  @NonNull
  public static String format(String message) {
    return message == null ? Strings.NULL : message;
  }

  /**
   * Build a message which has placeholders as the following:
   *
   * <p>"This message has a {0}"
   *
   * <p>"{0}" is the placeholder. It has to start from 0 and then scale up. The 0 represents the
   * index from the objects array. The placeholder will be replaced with the {@link
   * Object#toString()}
   *
   * @param message the message to format
   * @param strings the objects that will replace the placeholders
   * @return the formatted message
   */
  @NonNull
  public static String format(String message, Object... strings) {
    if (message != null) {
      for (int i = 0; i < strings.length; i++) {
        message =
            message.replace(
                "{" + i + "}", strings[i] == null ? Strings.NULL : strings[i].toString());
      }
    } else {
      message = Strings.NULL;
    }
    return message;
  }

  /**
   * Build a message using more readable placeholders. This uses a map with the placeholder and the
   * given object to replace it:
   *
   * <p>"This message has a %placeholder%"
   *
   * <p>"%placeholder%" is the placeholder that will be replaced with the object that it was given
   * in the map
   *
   * @param message the message to format
   * @param placeholders the placeholders and its values. The placeholders are the key and those do
   *     not require to have the character "%" and the value is another string
   * @return the formatted message
   */
  @NonNull
  public static String format(String message, @NonNull Map<String, String> placeholders) {
    if (message == null) {
      return Strings.NULL;
    }
    AtomicReference<String> atomicMessage = new AtomicReference<>(message);
    for (String placeholder : placeholders.keySet()) {
      String value = placeholders.get(placeholder);
      atomicMessage.set(
          atomicMessage
              .get()
              .replace("%" + placeholder + "%", value == null ? Strings.NULL : value));
    }
    return atomicMessage.get();
  }

  /**
   * Copy the matching strings from a list to a new one.
   *
   * <p>If you have the next string to match: "Hello" and your list contains the elements:
   *
   * <p>["Hello world", "Hello everyone", "What's up", "How's it going"]
   *
   * <p>This will copy the elements:
   *
   * <p>["Hello world", "Hello everyone"]
   *
   * @param toMatch The string to match the elements
   * @param list the list of the elements to match
   * @return the list with the matching elements
   */
  @NonNull
  public static List<String> copyPartials(@NonNull String toMatch, @NonNull List<String> list) {
    List<String> matching = new ArrayList<>();
    for (String string : list) {
      if (string.toLowerCase().startsWith(toMatch.toLowerCase())) {
        matching.add(string);
      }
    }
    return matching;
  }

  /**
   * Builds the aliases' usage from a collection of Strings.
   *
   * <p>If the collection looks like: ["hello", "world", "foo"]
   *
   * <p>The returning string will be: hello|world|foo
   *
   * @param aliases the collection of aliases for the string
   * @return the built string
   * @throws IllegalArgumentException if the collection is empty
   */
  @NonNull
  public static String buildUsageAliases(@NonNull Collection<String> aliases) {
    if (aliases.isEmpty()) {
      throw new IllegalArgumentException("Aliases collection may not be empty!");
    }
    StringBuilder builder = new StringBuilder();
    for (String alias : aliases) {
      builder.append(alias).append("|");
    }
    return builder.deleteCharAt(builder.length() - 1).toString();
  }

  /**
   * Builds the aliases' usage from an array of Strings.
   *
   * @param aliases the array of aliases for the string
   * @return the built string
   * @throws IllegalArgumentException if the array is empty
   * @see #buildUsageAliases(Collection)
   */
  public static String buildUsageAliases(@NonNull String... aliases) {
    return Strings.buildUsageAliases(Arrays.asList(aliases));
  }

  /**
   * Divides the given string to different strings of the given length.
   *
   * @param string the string to divide
   * @param length the length that each string must be
   * @return the list containing each string
   */
  @NonNull
  public static List<String> divide(@NonNull String string, int length) {
    List<String> split = new ArrayList<>();
    while (string.length() > length) {
      String substring = string.substring(0, length);
      string = string.substring(length);
      split.add(substring);
    }
    if (!string.isEmpty()) {
      split.add(string);
    }
    return split;
  }

  /**
   * Get the similarity between two string. 1f being that the two strings are the same and 0f being
   * that they are completely different.
   *
   * @param longer the first string
   * @param shorter the second string
   * @return the similarity between the two strings 0f being no similarity and 1f being that the two
   *     strings are the same
   */
  public static float similarity(@NonNull String longer, @NonNull String shorter) {
    if (longer.length() < shorter.length()) {
      String temp = longer;
      longer = shorter;
      shorter = temp;
    }
    float longerLength = longer.length();
    if (longerLength == 0) {
      return 1f;
    }
    return (longerLength - Strings.editDistance(longer, shorter)) / longerLength;
  }

  /**
   * Get the similarity between two strings ignoring casing.
   *
   * @param longer the first string
   * @param shorter the second string
   * @return the similarity between the two strings 0f being no similarity and 1f being that the two
   *     strings are the same
   * @see #similarity(String, String)
   */
  public static float similarityIgnoreCase(@NonNull String longer, @NonNull String shorter) {
    return Strings.similarity(longer.toLowerCase(), shorter.toLowerCase());
  }

  /**
   * Make a pretty strings from a {@link Collection}. This will check if the collection is empty, if
   * it is the case, the parameter empty will be returned else this will simply {@link
   * Object#toString()} the collection and replace the characters '[]' to nothing
   *
   * @param collection the collection to pretty
   * @param empty the string in case the collection is empty
   * @return the pretty collection string
   */
  @NonNull
  public static String pretty(@NonNull Collection<?> collection, @NonNull String empty) {
    return collection.isEmpty() ? empty : collection.toString().replace("[", "").replace("]", "");
  }

  /**
   * Make a pretty string from a {@link Collection}. If the collection is empty this will use '[]'
   *
   * @param collection the collection to pretty
   * @return the pretty collection string
   * @see #pretty(Collection, String)
   */
  @NonNull
  public static String pretty(@NonNull Collection<?> collection) {
    return Strings.pretty(collection, "[]");
  }

  /**
   * Make a pretty string from an array. This will check if the array is empty if it is the case,
   * the parameter empty will be returned else this will simple {@link Arrays#toString(int[])} the
   * array and replace the characters '[]' to nothing
   *
   * @param empty the string in case the array is empty
   * @param objects the array of object to pretty
   * @return the pretty array string
   */
  @NonNull
  public static String pretty(@NonNull String empty, @NonNull Object... objects) {
    return objects.length == 0 ? empty : Arrays.toString(objects);
  }

  /**
   * Make a pretty string from an array. If the collection is empty this will use '[]'
   *
   * @param objects the array of object to pretty
   * @return the pretty array string
   * @see #pretty(String, Object...)
   */
  @NonNull
  public static String pretty(@NonNull Object... objects) {
    return Strings.pretty("[]", objects);
  }

  /**
   * Generate a random strings with certain characters.
   *
   * @param random to create randomness
   * @param length the length that the string must be
   * @param chars the characters that may be used in the string
   * @return the random string
   */
  @NonNull
  public static String nextString(@NonNull Random random, int length, char... chars) {
    StringBuilder builder = new StringBuilder();
    while (builder.length() < length) {
      builder.append(chars[random.nextInt(chars.length)]);
    }
    return builder.toString();
  }

  /**
   * Generate a random string only using {@link #UPPERCASE} and {@link #LOWERCASE}.
   *
   * @param random to create randomness
   * @param length the length that the string must be
   * @return the random string
   */
  @NonNull
  public static String nextString(@NonNull Random random, int length) {
    return Strings.nextString(random, length, Strings.LETTERS);
  }

  /**
   * Generate a random string using a {@link Charset}.
   *
   * @param random to create randomness
   * @param length the length that the string must be
   * @param charset the charset of the characters that the string may contain
   * @return the random string
   */
  @NonNull
  public static String nextString(@NonNull Random random, int length, @NonNull Charset charset) {
    byte[] bytes = new byte[length];
    random.nextBytes(bytes);
    return new String(bytes, charset);
  }

  /**
   * Check the edit distance of two strings. This uses the Wagner-Fischer algorithm.
   *
   * @param longer the first string
   * @param shorter the second string
   * @return the edit distance between the two strings
   */
  private static int editDistance(String longer, String shorter) {
    int[] costs = new int[shorter.length() + 1];
    for (int i = 0; i <= longer.length(); i++) {
      int lastValue = i;
      for (int j = 0; j <= shorter.length(); j++) {
        if (i == 0) {
          costs[j] = j;
        } else {
          if (j > 0) {
            int newValue = costs[j - 1];
            if (longer.charAt(i - 1) != shorter.charAt(j - 1)) {
              newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
            }
            costs[j - 1] = lastValue;
            lastValue = newValue;
          }
        }
      }
      if (i > 0) {
        costs[shorter.length()] = lastValue;
      }
    }
    return costs[shorter.length()];
  }

  /**
   * Generates the usage of the command. Commands don't require a name, so, the base of the usage is
   * just the flags and arguments in case it is a {@link ReflectCommand}.
   *
   * @see Option#generateUsage(Collection)
   * @see Argument#generateUsage(List)
   * @param command the command to generate the help
   * @return the usage of the command
   */
  @NonNull
  public static String generateUsage(StarboxCommand<?, ?> command) {
    StringBuilder builder = new StringBuilder();
    builder.append(Option.generateUsage(command.getOptions()));
    if (command instanceof ReflectCommand) {
      ReflectCommand<?, ?> reflectCommand = (ReflectCommand<?, ?>) command;
      if (!reflectCommand.getArguments().isEmpty()) {
        builder.append(Argument.generateUsage(reflectCommand.getArguments()));
      }
    }
    return builder.toString();
  }

  /**
   * Get help for the command. This will generate a help message using {@link
   * #generateUsage(StarboxCommand)}
   *
   * @param command the command to generate the help
   * @param children the children of the command
   * @return the help message
   */
  public static String genericHelp(
      @NonNull StarboxCommand<?, ?> command,
      @NonNull Collection<? extends StarboxCommand<?, ?>> children) {
    StringBuilder builder = new StringBuilder();
    builder
        .append("usage: ")
        .append(command.getName())
        .append(" ")
        .append(Strings.generateUsage(command));
    if (!children.isEmpty()) {
      builder.append("\nSubcommands:");
      for (StarboxCommand<?, ?> child : children) {
        builder
            .append("\n + ")
            .append(child.getName())
            .append(" ")
            .append(Strings.generateUsage(child));
      }
    }
    return builder.toString();
  }

  /**
   * Get help for the command. This will generate a help message using {@link
   * #generateUsage(StarboxCommand)}
   *
   * @param command the command to generate the help
   * @param children the children of the command
   * @param nameSupplier the function that will supply the name of the command
   * @return the help message
   * @param <T> the type of command
   */
  public static <T extends StarboxCommand<?, ?>> String genericHelp(
      @NonNull T command,
      @NonNull Collection<T> children,
      @NonNull Function<T, String> nameSupplier) {
    StringBuilder builder = new StringBuilder();
    builder
        .append("usage: ")
        .append(nameSupplier.apply(command))
        .append(" ")
        .append(Strings.generateUsage(command));
    if (!children.isEmpty()) {
      builder.append("\nSubcommands:");
      for (T child : children) {
        builder
            .append("\n + ")
            .append(nameSupplier.apply(child))
            .append(" ")
            .append(Strings.generateUsage(child));
      }
    }
    return builder.toString();
  }
}
