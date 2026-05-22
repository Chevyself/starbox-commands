package com.github.chevyself.starbox.bukkit.topic;

import com.github.chevyself.starbox.bukkit.commands.BukkitCommand;
import com.github.chevyself.starbox.bukkit.messages.BukkitMessagesProvider;
import java.util.Collection;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;

/**
 * Command {@link HelpTopic} is basically the messages that appear in the command '/help
 * <command_name>'. This helps to create a {@link HelpTopic} for {@link StarboxCommandHelpTopic}
 *
 * <p>To know how this is formatted check {@link BukkitMessagesProvider} and also the constructor
 * {@link #StarboxCommandHelpTopic(StarboxBukkitCommand, StarboxCommandHelpTopic,
 * BukkitMessagesProvider)} has more detailed information about the {@link HelpTopic} {}
 */
@Setter
@Getter
class StarboxCommandHelpTopic extends HelpTopic {

  @NonNull private static final Server server = Bukkit.getServer();
  /** This instance of {@link HelpMap} is used to parseAndRegister topics for children commands. */
  @NonNull private static final HelpMap helpMap = StarboxCommandHelpTopic.server.getHelpMap();

  @NonNull private BukkitMessagesProvider provider;

  /**
   * Create the topic. The topic is created with {@link BukkitMessagesProvider}:
   *
   * <ul>
   *   <li>The name of the command = {@link BukkitMessagesProvider#commandName(StarboxBukkitCommand,
   *       String)}
   *   <li>The short text/summary of the command = {@link
   *       BukkitMessagesProvider#commandShortText(StarboxBukkitCommand)}
   *   <li>The full description of the command = {@link
   *       BukkitMessagesProvider#commandFullText(StarboxBukkitCommand, String, String, String)}
   * </ul>
   *
   * <p>For each children inside the command {@link StarboxCommand#getChildren()} another {@link
   * StarboxCommandHelpTopic} will be created
   *
   * @param command the command to create the topic from
   * @param parent the parent if the command has one
   * @param provider the messages' provider to format messages for the help topic
   */
  StarboxCommandHelpTopic(
      @NonNull BukkitCommand command,
      StarboxCommandHelpTopic parent,
      @NonNull BukkitMessagesProvider provider) {
    this.provider = provider;
    this.amendedPermission = StarboxCommandHelpTopic.getAmendedPermission(command);
    this.name = provider.commandName(command, parent == null ? null : parent.getName());
    this.shortText = provider.commandShortText(command);
    this.fullText = provider.commandFullText(command, this.buildChildren(command));
    for (BukkitCommand child : command.getChildren()) {
      StarboxCommandHelpTopic.helpMap.addTopic(new StarboxCommandHelpTopic(child, this, provider));
    }
  }

  /**
   * Get the permission that is used for the command {@link HelpTopic} in {@link
   * StarboxCommandHelpTopic}.
   *
   * @param command the command to get the permission from
   * @return the permission of the command
   */
  public static String getAmendedPermission(@NonNull BukkitCommand command) {
    String node = command.getPermission();
    return node == null ? null : node.isEmpty() ? null : node;
  }

  /**
   * Build the children help. This will use a {@link StringBuilder} and append {@link
   * BukkitMessagesProvider#childCommand(StarboxBukkitCommand, StarboxBukkitCommand)}
   *
   * @param command the parent to get the children help from
   * @return the help of the children command as string
   */
  @NonNull
  private String buildChildren(@NonNull BukkitCommand command) {
    StringBuilder builder = new StringBuilder();
    final Collection<BukkitCommand> commands = command.getChildren();
    for (BukkitCommand child : commands) {
      builder.append(this.provider.childCommand(child, command));
    }
    return builder.toString();
  }

  @Override
  public boolean canSee(@NonNull CommandSender commandSender) {
    if (this.amendedPermission == null) {
      return true;
    } else {
      return commandSender.hasPermission(this.amendedPermission);
    }
  }
}
