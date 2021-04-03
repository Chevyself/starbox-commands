package me.googas.commands.bukkit.utils;

import java.lang.reflect.Field;
import java.util.Map;
import lombok.NonNull;
import me.googas.utility.JsonUtils;
import me.googas.utility.Strings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

/**
 * Static utilities for bukkit:
 *
 * <p>Here you will find
 *
 * <ul>
 *   <li>String formatting
 *   <li>Command dispatcher
 * </ul>
 */
public class BukkitUtils {

  /**
   * Give colors to a message and format using {@link Strings#format(String)}.
   *
   * <p>Colors must be given with the color code char: '&amp;'
   *
   * <p>To know how to give colors to a message checkout <a
   * href="https://codepen.io/0biwan/full/ggVemP">this snippet</a>
   *
   * <p>A simple line to give colors should look like this:
   *
   * <ul>
   *   <li>'&amp;lHere's bold text'
   *   <li>'&amp;cThis is red text'
   *   <li>'&amp;5This is purple text'
   *   <li>'&amp;l&amp;6This is bold and gold text'
   * </ul>
   *
   * Here's each code of color:
   *
   * <ul>
   *   <li>'&amp;0' = Black
   *   <li>'&amp;1' = Dark blue
   *   <li>'&amp;2' = Dark green
   *   <li>'&amp;3' = Cyan
   *   <li>'&amp;4' = Dark red
   *   <li>'&amp;5' = Purple
   *   <li>'&amp;6' = Gold
   *   <li>'&amp;7' = Gray
   *   <li>'&amp;8' = Dark gray
   *   <li>'&amp;9' = Blue
   *   <li>'&amp;a' = Green
   *   <li>'&amp;b' = Aqua
   *   <li>'&amp;c' = Red
   *   <li>'&amp;d' = Magenta
   *   <li>'&amp;e' = Yellow
   *   <li>'&amp;f' = White
   *   <li>'&amp;l' = Bold
   *   <li>'&amp;n' = Underline
   *   <li>'&amp;o' = Italic
   *   <li>'&amp;m' = Strikethrough
   *   <li>'&amp;k' = Obfuscated
   *   <li>'&amp;r' = Resets the color
   * </ul>
   *
   * @param string the message to format
   * @return the formatted message
   */
  @NonNull
  public static String format(String string) {
    return ChatColor.translateAlternateColorCodes('&', Strings.format(string));
  }

  /**
   * Give colors to a message and format using {@link Strings#format(String, Map)}
   *
   * <p>To know how to use colors check {@link BukkitUtils#format(String)}
   *
   * @param string the message to format
   * @param placeholders the placeholders and its values. The placeholders are the key and those do
   *     not require to have the character "%" and the value is another string
   * @return the formatted message
   */
  @NonNull
  public static String format(String string, @NonNull Map<String, String> placeholders) {
    return ChatColor.translateAlternateColorCodes('&', Strings.format(string, placeholders));
  }

  /**
   * Gives colors to a message and format using {@link Strings#format(String, Object...)}
   *
   * <p>To know how to use colors check {@link BukkitUtils#format(String)}
   *
   * @param message the message to format
   * @param strings the objects that will replace the placeholders
   * @return the formatted message
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
   * Parse the JSON string into an array of {@link BaseComponent}. This will check that the String
   * is JSON with {@link JsonUtils#isJson(String)} if it is then it will use {@link
   * ComponentSerializer#parse(String)} to get the array else it will return an array of {@link
   * BaseComponent} with a single entry of {@link TextComponent}
   *
   * @param string the string to get the array from
   * @return the parsed component
   */
  @NonNull
  public static BaseComponent[] getComponent(@NonNull String string) {
    if (JsonUtils.isJson(string)) {
      return ComponentSerializer.parse(string);
    }
    return new BaseComponent[] {new TextComponent(string)};
  }

  /**
   * Get Bukkit {@link CommandMap} it is used to register commands and to get it reflection need to
   * be used:
   *
   * <p>Getting the {@link Server} class this will get the declared field 'commandMap' which
   * contains the {@link CommandMap}
   *
   * @return the command map
   * @throws IllegalAccessException in case that the field 'commandMap' in {@link Server} cannot be
   *     accessed
   * @throws NoSuchFieldException in case the field 'commandMap' in {@link Server} does not exist
   *     for some reason
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
