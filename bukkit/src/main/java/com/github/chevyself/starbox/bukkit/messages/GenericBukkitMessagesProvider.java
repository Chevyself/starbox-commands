package com.github.chevyself.starbox.bukkit.messages;

import com.github.chevyself.starbox.bukkit.commands.BukkitCommand;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.utils.BukkitUtils;
import com.github.chevyself.starbox.common.DecoratedMessagesProvider;
import com.github.chevyself.starbox.util.Strings;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

/** The default {@link BukkitMessagesProvider} for Bukkit. */
public class GenericBukkitMessagesProvider extends DecoratedMessagesProvider<CommandContext>
    implements BukkitMessagesProvider {

  @NonNull
  @Override
  public String notAllowed(@NonNull CommandContext context) {
    return String.format(
        "%s%sYou are not allowed to use this command", this.errorPrefix(), this.text());
  }

  @Override
  public @NonNull String invalidPlayer(@NonNull String string, @NonNull CommandContext context) {
    return String.format(
        "%s%s%s %sis not a valid player", this.errorPrefix(), this.element(), string, this.text());
  }

  @Override
  public @NonNull String onlyPlayers(@NonNull CommandContext context) {
    return String.format("%s%sOnly players can use this command", this.errorPrefix(), this.text());
  }

  @Override
  public @NonNull String helpTopicShort(@NonNull Plugin plugin) {
    return "Get help for " + plugin.getName();
  }

  @Override
  public @NonNull String helpTopicFull(
      @NonNull String shortText, @NonNull String commands, @NonNull Plugin plugin) {
    String description =
        plugin.getDescription().getDescription() == null
            ? "No description given"
            : plugin.getDescription().getDescription();
    return BukkitUtils.format(
        "&6Version: &f{0} \n &6Description: &f{1} \n &7Commands (use /help <command>): {2}",
        plugin.getDescription().getVersion(), description, commands);
  }

  @Override
  public @NonNull String helpTopicCommand(@NonNull BukkitCommand command) {
    List<String> aliases = new ArrayList<>(command.getAliases());
    aliases.add(command.getName());
    return BukkitUtils.format(
        "\n&6/{0}: &f{1}",
        Strings.buildUsageAliases(aliases), command.getExecutor().getDescription());
  }

  @Override
  public @NonNull String commandShortText(@NonNull BukkitCommand command) {
    return command.getExecutor().getDescription();
  }

  @Override
  public @NonNull String commandName(@NonNull BukkitCommand command, String parentName) {
    return (parentName == null
        ? "/" + command.getName()
        : (parentName.startsWith("/") ? "" : "/") + parentName + "." + command.getName());
  }

  @Override
  public @NonNull String commandFullText(
      @NonNull BukkitCommand command, @NonNull String builtChildren) {
    StringBuilder builder =
        new StringBuilder()
            .append("&6Description: &f")
            .append(command.getExecutor().getDescription())
            .append("\n&6Usage: &f")
            .append(command.getExecutor().getUsage());
    if (command.getPermission() != null) {
      builder.append("\n&6Permission: &f").append(command.getPermission());
    }
    if (!command.getChildren().isEmpty()) {
      builder.append("\n&6Children: &r").append(builtChildren);
    }
    return BukkitUtils.format(builder.toString());
  }

  @Override
  public @NonNull String childCommand(
      @NonNull BukkitCommand command, @NonNull BukkitCommand parent) {
    return BukkitUtils.format(
        "\n&6/{0} {1}: &f{2}",
        parent.getName(), command.getName(), command.getExecutor().getDescription());
  }

  @Override
  public @NonNull String invalidMaterialEmpty(@NonNull CommandContext context) {
    return String.format("%s%sMaterial cannot be empty", this.errorPrefix(), this.text());
  }

  @Override
  public @NonNull String invalidMaterial(@NonNull String string, @NonNull CommandContext context) {
    return String.format(
        "%s%s %sis not a valid material", this.errorPrefix(), this.element(), this.text());
  }
}
