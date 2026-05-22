package com.github.chevyself.starbox.velocity.result;

import com.github.chevyself.starbox.result.type.SimpleResult;
import com.github.chevyself.starbox.velocity.util.Components;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.text.Component;

/**
 * Extension of result that allows the use of the Minecraft component system with the adventure API.
 */
@Getter
public class ComponentResult extends SimpleResult {
  /** The components that will be sent after the execution. */
  @NonNull private final List<Component> components;

  /**
   * Create an instance.
   *
   * @param components the components to send
   */
  public ComponentResult(@NonNull List<Component> components) {
    super(Components.toString(components));
    this.components = components;
  }

  /**
   * Create an instance.
   *
   * @param components the components to send
   */
  public ComponentResult(@NonNull Component... components) {
    this(Arrays.asList(components));
  }
}
