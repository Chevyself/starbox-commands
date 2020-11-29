package com.starfishst.bukkit.topic;

import com.starfishst.bukkit.CommandManager;
import com.starfishst.bukkit.messages.MessagesProvider;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commons.Strings;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.Plugin;

/** The help topic for the plugin that is using the framework */
public class PluginHelpTopic extends HelpTopic {

  /** The command manager that has the plugin that its help topic is being created */
  @NonNull @Getter private final CommandManager manager;
  /** The messages provider to build messages for the help topic */
  @NonNull @Getter private final MessagesProvider provider;

  /**
   * Create the help topic
   *
   * @param plugin the plugin that is using the framework
   * @param manager the plugin manager that has the plugin
   * @param provider the messages provider to build the messages
   */
  public PluginHelpTopic(
      @NonNull Plugin plugin, @NonNull CommandManager manager, @NonNull MessagesProvider provider) {
    this.manager = manager;
    this.provider = provider;
    this.amendedPermission = plugin.getName() + ".help";
    this.name = plugin.getName();
    this.shortText = this.provider.helpTopicShort(plugin);
    this.fullText = this.provider.helpTopicFull(this.shortText, this.getCommands(), plugin);
  }

  /**
   * Get all the commands that the plugin has
   *
   * @return the commands that the plugin has
   */
  @NonNull
  private String getCommands() {
    StringBuilder builder = Strings.getBuilder();
    this.manager
        .getCommands()
        .forEach(command -> builder.append(this.provider.helpTopicCommand(command)));
    return builder.toString();
  }

  @Override
  public boolean canSee(@NonNull CommandSender commandSender) {
    return commandSender.hasPermission(this.amendedPermission);
  }
}
