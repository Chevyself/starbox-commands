package me.googas.commands.bukkit.utils;

import java.lang.reflect.Field;
import java.util.Map;
import lombok.NonNull;
import me.googas.commands.utility.JsonUtils;
import me.googas.commands.utility.Strings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

/** Utils for bukkit */
public class BukkitUtils {
  /**
   * Build a message and give it colors
   *
   * @param string the message to format
   * @param placeholders the placeholders of the message. See {@link Strings#format(String)}
   *     (String, Map)}
   * @return the built message
   */
  @NonNull
  public static String format(String string, @NonNull Map<String, String> placeholders) {
    return ChatColor.translateAlternateColorCodes('&', Strings.format(string, placeholders));
  }

  /**
   * Build a message and give it colors
   *
   * @param string the message to format
   * @return the built message
   */
  @NonNull
  public static String format(String string) {
    return ChatColor.translateAlternateColorCodes('&', Strings.format(string));
  }

  /**
   * Creates a message with colors and place holders
   *
   * @param message the message
   * @param strings the place holders
   * @return the built colored message
   */
  @NonNull
  public static String format(String message, Object... strings) {
    return BukkitUtils.format(Strings.format(message, strings));
  }

  /**
   * Dispatch a command
   *
   * @param sender the sender of the command
   * @param command the command to send
   * @param objects to change the placeholders in the command
   */
  public static void dispatch(
      @NonNull CommandSender sender, @NonNull String command, Object... objects) {
    dispatch(sender, Strings.format(command, objects));
  }

  /**
   * Dispatch a command
   *
   * @param sender the sender of the command
   * @param command the command to send
   * @param placeholders the placeholders to change in the command line
   */
  public static void dispatch(
      @NonNull CommandSender sender,
      @NonNull String command,
      @NonNull Map<String, String> placeholders) {
    dispatch(sender, Strings.format(command, placeholders));
  }

  /**
   * Dispatch a command
   *
   * @param sender the sender of the command
   * @param command the command to send
   */
  public static void dispatch(@NonNull CommandSender sender, @NonNull String command) {
    Bukkit.dispatchCommand(sender, command);
  }

  /**
   * Dispatch a command as console
   *
   * @param command the command to send
   * @param objects to change the placeholders in the command
   */
  public static void dispatch(@NonNull String command, Object... objects) {
    dispatch(Strings.format(command, objects));
  }

  /**
   * Dispatch a command as console
   *
   * @param command the command to send
   * @param placeholders the placeholders to change in the command line
   */
  public static void dispatch(@NonNull String command, Map<String, String> placeholders) {
    dispatch(Strings.format(command, placeholders));
  }

  /**
   * Dispatch a command as console
   *
   * @param command the command to dispatch
   */
  public static void dispatch(@NonNull String command) {
    dispatch(Bukkit.getConsoleSender(), command);
  }

  /**
   * Parse the json into a component
   *
   * @param string the string to get the component from
   * @return the component
   */
  @NonNull
  public static BaseComponent[] getComponent(@NonNull String string) {
    if (JsonUtils.isJson(string)) {
      return ComponentSerializer.parse(string);
    }
    return new BaseComponent[] {new TextComponent(string)};
  }

  /**
   * Get bukkit command map
   *
   * @return bukkit command map
   * @throws IllegalAccessException in case that the field commandMap in {@link Server} cannot be
   *     accessed
   * @throws NoSuchFieldException in case the field commandMap in {@link Server} cannot be accessed
   */
  @NonNull
  public static CommandMap getCommandMap() throws NoSuchFieldException, IllegalAccessException {
    final Server server = Bukkit.getServer();
    final Class<? extends Server> serverClass = server.getClass();
    final Field commandMapField = serverClass.getDeclaredField("commandMap");
    commandMapField.setAccessible(true);
    return (CommandMap) commandMapField.get(server);
  }
}
