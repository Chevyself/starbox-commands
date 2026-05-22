package com.github.chevyself.starbox.velocity.util;

import com.github.chevyself.starbox.common.JsonUtils;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.velocity.result.ComponentResult;
import java.util.List;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/** Static utilities for {@link Component}. */
public final class Components {

  /**
   * Convert an array of {@link Component} to a plain string.
   *
   * @param components the components to convert
   * @return the plain string
   */
  @NonNull
  public static String toString(@NonNull List<Component> components) {
    StringBuilder builder = new StringBuilder();
    for (Component component : components) {
      builder.append(LegacyComponentSerializer.legacyAmpersand().serialize(component));
    }
    return builder.toString();
  }

  /**
   * Convert a plain string to components and then as a {@link Result}.
   *
   * @param plain the plain string to convert
   * @return the result
   */
  @NonNull
  public static ComponentResult asResult(@NonNull String plain) {
    Component component;
    if (JsonUtils.isJson(plain)) {
      component = GsonComponentSerializer.gson().deserialize(plain);
    } else {
      component = LegacyComponentSerializer.legacy('&').deserialize(plain);
    }
    return Components.asResult(component);
  }

  /**
   * Convert a {@link Component} to a {@link Result}.
   *
   * @param component the component to convert
   * @return the result
   */
  @NonNull
  public static ComponentResult asResult(@NonNull Component component) {
    return new ComponentResult(component);
  }
}
