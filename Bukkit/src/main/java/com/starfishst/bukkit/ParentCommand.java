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
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;

/** The annotated parent command for bukkit */
public class ParentCommand extends AnnotatedCommand implements IParentCommand<AnnotatedCommand> {

  /** The command manager options */
  @NonNull @Getter private final CommandManagerOptions options;
  /** The list of children commands */
  @NonNull private final List<AnnotatedCommand> commands = new ArrayList<>();
  /** The alias of the command */
  @NonNull private final List<String> commandsAlias = new ArrayList<>();

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
      @NonNull Object clazz,
      @NonNull Method method,
      @NonNull List<ISimpleArgument<?>> arguments,
      @NonNull Command command,
      @NonNull CommandManagerOptions options,
      @NonNull MessagesProvider messagesProvider,
      @NonNull Plugin plugin,
      boolean async,
      @NonNull ProvidersRegistry<CommandContext> registry) {
    super(clazz, method, arguments, command, messagesProvider, plugin, async, registry);
    this.options = options;
  }

  @Override
  public @NonNull List<AnnotatedCommand> getCommands() {
    return this.commands;
  }

  @Override
  public AnnotatedCommand getCommand(@NonNull String name) {
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
  public void addCommand(@NonNull AnnotatedCommand command) {
    this.commands.add(command);
    this.commandsAlias.add(command.getName());

    if (this.options.isIncludeAliases()) {
      this.commandsAlias.addAll(command.getAliases());
    }
  }

  @Override
  public boolean execute(
      @NonNull CommandSender commandSender, @NonNull String s, @NonNull String[] strings) {
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

  @NonNull
  @Override
  public List<String> tabComplete(
      @NonNull final CommandSender sender,
      @NonNull final String alias,
      @NonNull final String[] strings)
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
