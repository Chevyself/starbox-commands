package com.github.chevyself.starbox.parsers;

import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import lombok.NonNull;

/**
 * Supplies the parser with a parent command. This is used for classes that are annotated with
 * {@link Command}
 *
 * @param <C> the type of the context
 * @param <T> the type of the command
 */
public interface ParentCommandSupplier<
    C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  /**
   * Supplies the command. The generic implementation is a parent command which extends the abstract
   * class {@link com.github.chevyself.starbox.commands.AbstractParentCommand}
   *
   * @param annotation the command annotation of the class
   * @param clazz the class that is annotated with {@link Command}
   * @return the parent command
   */
  @NonNull
  T supply(@NonNull Command annotation, @NonNull Class<?> clazz);
}
