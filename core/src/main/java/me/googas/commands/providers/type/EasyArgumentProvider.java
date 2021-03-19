package me.googas.commands.providers.type;

import lombok.NonNull;
import me.googas.commands.EasyCommand;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;

/**
 * A provider gives to the {@link EasyCommand} the object requested by it
 *
 * @param <O> the class to provide as argument
 */
public interface EasyArgumentProvider<O, T extends EasyCommandContext>
    extends EasyContextualProvider<O, T> {

  /**
   * Get the class instance for the {@link EasyCommand}
   *
   * @param string the string to get the object from
   * @param context the context used in the command
   * @return a new instance of {@link O}
   * @throws ArgumentProviderException when the object could not be obtained
   */
  @NonNull
  O fromString(@NonNull String string, @NonNull T context) throws ArgumentProviderException;
}