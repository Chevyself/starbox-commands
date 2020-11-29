package com.starfishst.jda.permissions;

import com.starfishst.jda.annotations.Command;
import lombok.NonNull;
import net.dv8tion.jda.api.Permission;

/**
 * This object represents a permission which is created with {@link Command#node()} and {@link
 * Command#permission()}
 */
public class SimplePermission implements JdaPermission {

  /** @see Command#node() */
  @NonNull private final String node;

  /** @see Command#permission() */
  @NonNull private final Permission permission;

  public SimplePermission(@NonNull String node, @NonNull Permission permission) {
    this.node = node;
    this.permission = permission;
  }

  @Override
  public @NonNull String getNode() {
    return this.node;
  }

  @Override
  public @NonNull Permission getPermission() {
    return this.permission;
  }
}
