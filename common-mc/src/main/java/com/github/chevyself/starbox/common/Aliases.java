package com.github.chevyself.starbox.common;

import com.github.chevyself.starbox.commands.StarboxCommand;
import java.util.Collection;
import lombok.NonNull;

/** Static utility class for aliases. */
public final class Aliases {

  /**
   * Convert a collection of aliases to an array of aliases.
   *
   * @param aliases the collection of aliases
   * @return the array of aliases
   */
  @NonNull
  public static String[] getAliases(@NonNull Collection<String> aliases) {
    String[] aliasesArray = new String[aliases.size() - 1];
    for (int i = 1; i < aliases.size(); i++) {
      aliasesArray[i - 1] = aliases.toArray(new String[0])[i];
    }
    return aliasesArray;
  }

  /**
   * Get the aliases of a command.
   *
   * @param command the command
   * @return the aliases of the command
   */
  @NonNull
  public static String[] getAliases(@NonNull StarboxCommand<?, ?> command) {
    return Aliases.getAliases(command.getAliases());
  }
}
