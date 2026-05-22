package com.github.chevyself.starbox.common;

import com.github.chevyself.starbox.result.type.SimpleResult;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * Extension of result that allows the use of the Minecraft component system with the Bungee chat
 * API.
 */
@Getter
public class ComponentResult extends SimpleResult {
  /** The components that will be sent after the execution. */
  @NonNull private final List<BaseComponent> components;

  /**
   * Create an instance.
   *
   * @param components the components to send
   */
  public ComponentResult(@NonNull List<BaseComponent> components) {
    super(ComponentResult.toString(components));
    this.components = components;
  }

  /**
   * Create an instance.
   *
   * @param components the components to send
   */
  public ComponentResult(@NonNull BaseComponent[] components) {
    this(Arrays.asList(components));
  }

  @NonNull
  private static String toString(@NonNull List<BaseComponent> components) {
    if (components.isEmpty()) {
      return "[]";
    }
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    for (BaseComponent component : components) {
      builder.append(ComponentSerializer.toString(component)).append(", ");
    }
    builder.delete(builder.length() - 2, builder.length());
    builder.append("]");
    return builder.toString();
  }
}
