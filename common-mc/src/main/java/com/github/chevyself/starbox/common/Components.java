package com.github.chevyself.starbox.common;

import com.github.chevyself.starbox.result.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/** Static utilities for {@link BaseComponent}. */
public final class Components {

  @NonNull
  private static final Map<Character, Modifier> modifiers =
      new MapBuilder<Character, Modifier>()
          .put('0', new ColorModifier(ChatColor.BLACK))
          .put('1', new ColorModifier(ChatColor.BLUE))
          .put('2', new ColorModifier(ChatColor.DARK_GREEN))
          .put('3', new ColorModifier(ChatColor.DARK_AQUA))
          .put('4', new ColorModifier(ChatColor.DARK_RED))
          .put('5', new ColorModifier(ChatColor.DARK_PURPLE))
          .put('6', new ColorModifier(ChatColor.GOLD))
          .put('7', new ColorModifier(ChatColor.GRAY))
          .put('8', new ColorModifier(ChatColor.DARK_GRAY))
          .put('9', new ColorModifier(ChatColor.BLUE))
          .put('a', new ColorModifier(ChatColor.GREEN))
          .put('b', new ColorModifier(ChatColor.AQUA))
          .put('c', new ColorModifier(ChatColor.RED))
          .put('d', new ColorModifier(ChatColor.LIGHT_PURPLE))
          .put('e', new ColorModifier(ChatColor.YELLOW))
          .put('f', new ColorModifier(ChatColor.WHITE))
          .put('k', component -> component.setObfuscated(true))
          .put('l', component -> component.setBold(true))
          .put('m', component -> component.setStrikethrough(true))
          .put('n', component -> component.setUnderlined(true))
          .put('o', component -> component.setItalic(true))
          .build();

  /**
   * Convert plain text to Components and then as a {@link Result}.
   *
   * @param plain the plain text to convert
   * @return the result
   */
  @NonNull
  public static ComponentResult asResult(@NonNull String plain) {
    return new ComponentResult(Components.getComponent(plain));
  }

  /**
   * Convert an array of {@link BaseComponent} to a {@link Result}.
   *
   * @param components the components to convert
   * @return the result
   */
  @NonNull
  public static ComponentResult asResult(@NonNull BaseComponent[] components) {
    return new ComponentResult(components);
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
    } else {
      return Components.deserializePlain('&', string);
    }
  }

  /**
   * Convert plaint text to an array of {@link BaseComponent}.
   *
   * @param modifierChar the char that represents the colors and modifiers
   * @param text the text to deserialize
   * @return the components
   */
  @NonNull
  public static BaseComponent[] deserializePlain(char modifierChar, @NonNull String text) {
    List<BaseComponent> components = new ArrayList<>();
    TextComponent current = new TextComponent();
    StringBuilder content = new StringBuilder();
    boolean modify = false;
    ColorModifier color = null;
    Modifier modifier = null;
    for (char c : text.toCharArray()) {
      if (c == modifierChar) {
        modify = true;
        continue;
      } else if (modify) {
        modify = false;
        Modifier modification = Components.modifiers.get(Character.toLowerCase(c));
        boolean reset;
        if (modification != null) {
          if (modification instanceof ColorModifier) {
            reset = color == null || !color.equals(modification);
            color = (ColorModifier) modification;
            if (!reset) {
              color.modify(current);
            }
          } else {
            reset = modifier == null || !modifier.equals(modification);
            modifier = modification;
            if (!reset) {
              modifier.modify(current);
            }
          }
        } else if (c == 'r') {
          reset = true;
          color = null;
          modifier = null;
        } else {
          content.append(modifierChar).append(c);
          continue;
        }
        if (reset) {
          current = Components.reset(components, current, content, color, modifier);
        }
        continue;
      }
      content.append(c);
    }
    current.setText(content.toString());
    components.add(current);
    return components.toArray(new BaseComponent[0]);
  }

  @NonNull
  private static TextComponent reset(
      List<BaseComponent> components,
      TextComponent current,
      StringBuilder content,
      ColorModifier color,
      Modifier modifier) {
    current.setText(content.toString());
    components.add(current);
    content.setLength(0);
    current = new TextComponent();
    if (color != null) {
      color.modify(current);
    }
    if (modifier != null) {
      modifier.modify(current);
    }
    return current;
  }

  @NonNull
  private interface Modifier {

    void modify(@NonNull TextComponent component);
  }

  @NonNull
  private static class ColorModifier implements Modifier {

    @NonNull private final ChatColor color;

    public ColorModifier(@NonNull ChatColor color) {
      this.color = color;
    }

    @Override
    public void modify(@NonNull TextComponent component) {
      component.setColor(color);
    }
  }

  private static class MapBuilder<K, V> {

    @NonNull private final Map<K, V> map;

    private MapBuilder(@NonNull Map<K, V> map) {
      this.map = map;
    }

    private MapBuilder() {
      this(new HashMap<>());
    }

    @NonNull
    private MapBuilder<K, V> put(@NonNull K key, @NonNull V value) {
      map.put(key, value);
      return this;
    }

    @NonNull
    private Map<K, V> build() {
      return new HashMap<>(this.map);
    }
  }
}
