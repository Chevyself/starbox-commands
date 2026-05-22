package com.github.chevyself.starbox.bukkit.topic;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.bukkit.commands.BukkitCommand;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.messages.BukkitMessagesProvider;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.Plugin;

/**
 * Plugin {@link HelpTopic} is basically the messages that appear in the command '/help
 * &lt;plugin_name&gt;'. This helps to create a {@link HelpTopic} for {@link
 * org.bukkit.plugin.java.JavaPlugin} that uses {@link CommandManager} to parseAndRegister commands
 */
@Getter
public class PluginHelpTopic extends HelpTopic {

  @NonNull private final CommandManager<CommandContext, BukkitCommand> manager;
  @NonNull private final BukkitMessagesProvider provider;

  /**
   * Create the instance of the help topic.
   *
   * @param plugin the plugin that is using the framework
   * @param manager the command manager that the plugin uses
   * @param provider the messages' provider to format the messages see {@link
   *     BukkitMessagesProvider#helpTopicShort(Plugin)}, {@link
   *     BukkitMessagesProvider#helpTopicFull(String, String, Plugin)}
   */
  public PluginHelpTopic(
      @NonNull Plugin plugin,
      @NonNull CommandManager<CommandContext, BukkitCommand> manager,
      @NonNull BukkitMessagesProvider provider) {
    this.manager = manager;
    this.provider = provider;
    this.amendedPermission = plugin.getName().replace(" ", "_") + ".help";
    this.name = plugin.getName();
    this.shortText = this.provider.helpTopicShort(plugin);
    this.fullText = this.provider.helpTopicFull(this.shortText, this.getCommands(), plugin);
  }

  /**
   * Gets the message to include in {@link BukkitMessagesProvider#helpTopicFull(String, String,
   * Plugin)}, Using a {@link StringBuilder} getting all the commands registered in {@link #manager}
   * and adding its {@link BukkitMessagesProvider#helpTopicCommand(StarboxBukkitCommand)} to the
   * builder.
   *
   * @return the message that includes the commands for {@link
   *     BukkitMessagesProvider#helpTopicFull(String, String, Plugin)}}
   */
  @NonNull
  private String getCommands() {
    StringBuilder builder = new StringBuilder();
    for (BukkitCommand command : this.manager.getCommands()) {
      builder.append(this.provider.helpTopicCommand(command));
    }
    return builder.toString();
  }

  @Override
  public boolean canSee(@NonNull CommandSender commandSender) {
    return commandSender.hasPermission(this.amendedPermission);
  }
}
