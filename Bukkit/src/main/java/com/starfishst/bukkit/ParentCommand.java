package com.starfishst.bukkit;

import com.starfishst.bukkit.annotations.Command;
import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.core.IParentCommand;
import com.starfishst.core.arguments.ISimpleArgument;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** The annotated parent command for bukkit */
public class ParentCommand extends AnnotatedCommand implements IParentCommand<AnnotatedCommand> {

  /** The command manager options */
  @NotNull private final CommandManagerOptions options;
  /** The list of children commands */
  @NotNull private final List<AnnotatedCommand> commands = new ArrayList<>();
  /** The alias of the command */
  @NotNull private final List<String> commandsAlias = new ArrayList<>();

  /**
   * Create an instance
   *
   * @param clazz the object that contains the method to invoke
   * @param method the method to invoke
   * @param arguments the arguments to get the parameters for the method
   * @param command the command annotation
   * @param options the options for the command
   * @param messagesProvider the messages provider
   * @param plugin the plugin where this command was registered
   * @param async whether this command should execute asynchronously
   * @param registry the providers registry for the command context
   */
  ParentCommand(
      @NotNull Object clazz,
      @NotNull Method method,
      @NotNull List<ISimpleArgument<?>> arguments,
      @NotNull Command command,
      @NotNull CommandManagerOptions options,
      @NotNull MessagesProvider messagesProvider,
      @NotNull Plugin plugin,
      boolean async,
      @NotNull ProvidersRegistry<CommandContext> registry) {
    super(clazz, method, arguments, command, messagesProvider, plugin, async, registry);
    this.options = options;
  }

  @Override
  public @NotNull List<AnnotatedCommand> getCommands() {
    return this.commands;
  }

  @Override
  public @Nullable AnnotatedCommand getCommand(@NotNull String name) {
    return this.commands.stream()
        .filter(
            annotatedCommand ->
                annotatedCommand.getName().equalsIgnoreCase(name)
                    || annotatedCommand.getAliases().stream()
                            .filter(alias -> alias.equalsIgnoreCase(name))
                            .findFirst()
                            .orElse(null)
                        != null)
        .findFirst()
        .orElse(null);
  }

  @Override
  public void addCommand(@NotNull AnnotatedCommand command) {
    this.commands.add(command);
    this.commandsAlias.add(command.getName());

    if (this.options.isIncludeAliases()) {
      this.commandsAlias.addAll(command.getAliases());
    }
  }

  @Override
  public boolean execute(
      @NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
    if (strings.length >= 1) {
      AnnotatedCommand command = this.getCommand(strings[0]);
      if (command != null) {
        return command.execute(commandSender, s, Arrays.copyOfRange(strings, 1, strings.length));
      } else {
        return super.execute(commandSender, s, strings);
      }
    } else {
      return super.execute(commandSender, s, strings);
    }
  }

  @NotNull
  @Override
  public List<String> tabComplete(
      @NotNull final CommandSender sender,
      @NotNull final String alias,
      @NotNull final String[] strings)
      throws IllegalArgumentException {
    if (strings.length == 1) {
      return StringUtil.copyPartialMatches(
          strings[strings.length - 1], this.commandsAlias, new ArrayList<>());
    } else if (strings.length >= 2) {
      final AnnotatedCommand command = this.getCommand(strings[0]);
      if (command != null) {
        return command.tabComplete(sender, alias, Arrays.copyOfRange(strings, 1, strings.length));
      } else {
        return super.tabComplete(sender, alias, Arrays.copyOfRange(strings, 1, strings.length));
      }
    } else {
      return new ArrayList<>();
    }
  }
}
