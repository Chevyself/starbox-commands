package com.starfishst.bukkit.providers.type;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.core.providers.type.IMultipleArgumentProvider;
import java.util.List;
import lombok.NonNull;

/** It's a provider made for bukkit commands */
public interface BukkitMultiArgumentProvider<O>
    extends IMultipleArgumentProvider<O, CommandContext> {

  /**
   * Get the suggestions for the command
   *
   * @param context the context of the command
   * @return a list of suggestions of the command
   */
  @NonNull
  List<String> getSuggestions(@NonNull CommandContext context);
}
