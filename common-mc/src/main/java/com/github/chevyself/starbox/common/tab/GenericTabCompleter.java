package com.github.chevyself.starbox.common.tab;

import com.github.chevyself.starbox.arguments.SingleArgument;
import com.github.chevyself.starbox.commands.ReflectCommand;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import com.github.chevyself.starbox.providers.StarboxArgumentProvider;
import com.github.chevyself.starbox.util.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;

/**
 * Abstract extension of tab complete that fulfills the logic of tab completing a command.
 *
 * @param <C> the command context
 * @param <T> the command
 * @param <O> the sender
 */
public abstract class GenericTabCompleter<
        C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>, O>
    implements TabCompleter<C, T, O> {

  /**
   * Get the permission to run the parameter command.
   *
   * @param command the command to get the permission from
   * @return the permission to run the command
   */
  public abstract String getPermission(@NonNull T command);

  /**
   * Check if the sender has the permission to run the command.
   *
   * @param sender the sender
   * @param permission the permission to check
   * @return if the sender has the permission
   */
  public abstract boolean hasPermission(@NonNull O sender, @NonNull String permission);

  /**
   * Create the command context.
   *
   * @param parser the command line parser
   * @param command the command to run
   * @param sender the sender
   * @return the command context
   */
  @NonNull
  public abstract C createContext(
      @NonNull CommandLineParser parser, @NonNull T command, @NonNull O sender);

  private @NonNull Optional<SingleArgument<?>> getArgument(@NonNull T command, int index) {
    if (command instanceof ReflectCommand) {
      return ((ReflectCommand<?, ?>) command).getArgument(index);
    } else {
      return Optional.empty();
    }
  }

  /**
   * Use reflection to generate tab complete suggestions for the last argument.
   *
   * @param command the command to tab complete
   * @param sender the sender
   * @param strings the arguments
   * @return the tab complete suggestions
   */
  public @NonNull List<String> reflectTabComplete(
      @NonNull T command, @NonNull O sender, @NonNull String[] strings) {
    CommandLineParser parser = CommandLineParser.parse(command.getOptions(), strings);
    C context = this.createContext(parser, command, sender);
    Optional<SingleArgument<?>> optionalArgument = this.getArgument(command, strings.length - 1);
    if (optionalArgument.isPresent()) {
      SingleArgument<?> argument = optionalArgument.get();
      if (!argument.getSuggestions(context).isEmpty()) {
        return Strings.copyPartials(strings[strings.length - 1], argument.getSuggestions(context));
      } else {
        StarboxArgumentProvider<?, C> provider =
            command.getProvidersRegistry().getProvider(argument.getClazz());
        if (provider instanceof SuggestionsArgumentProvider) {
          return Strings.copyPartials(
              strings[strings.length - 1],
              ((SuggestionsArgumentProvider<?, C>) provider)
                  .getSuggestions(strings[strings.length - 1], context));
        }
        return new ArrayList<>();
      }
    } else {
      return new ArrayList<>();
    }
  }

  @Override
  public @NonNull List<String> tabComplete(
      @NonNull T command, @NonNull O sender, @NonNull String name, @NonNull String[] strings) {
    String permission = this.getPermission(command);
    if (permission != null && !this.hasPermission(sender, permission)) {
      return new ArrayList<>();
    }
    if (strings.length == 1 || strings.length == 0) {
      List<String> children =
          strings.length == 0
              ? command.getChildrenNames()
              : Strings.copyPartials(strings[0], command.getChildrenNames());
      children.addAll(this.reflectTabComplete(command, sender, strings));
      return children;
    } else {
      return command
          .getChild(strings[0])
          .map(
              child ->
                  this.tabComplete(
                      child, sender, strings[0], Arrays.copyOfRange(strings, 1, strings.length)))
          .orElseGet(() -> this.reflectTabComplete(command, sender, strings));
    }
  }
}
