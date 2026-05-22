package com.github.chevyself.starbox.arguments;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.exceptions.MissingArgumentException;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import com.github.chevyself.starbox.util.Pair;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;

/**
 * This argument is not exactly given by the user but by the context of the command execution, that
 * is why it does not require an annotation as you can see in {@link Argument#isEmpty(Annotation[])}
 * if this method returns true it will be considered as an {@link ExtraArgument}
 *
 * <p>The first example of an extra argument is the context of the command execution another two
 * examples could be the location of the Minecraft player that ran the command or the text channel
 * where the command was sent in Discord.
 *
 * @param <O> the type of the class that the argument has to supply
 */
@Getter
public final class ExtraArgument<O> implements Argument<O> {

  @NonNull private final Class<O> clazz;

  /**
   * Create a new extra argument instance.
   *
   * @param clazz the class of the argument
   */
  public ExtraArgument(@NonNull Class<O> clazz) {
    this.clazz = clazz;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ExtraArgument.class.getSimpleName() + "[", "]")
        .add("clazz=" + clazz)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ExtraArgument<?> that = (ExtraArgument<?>) o;
    return clazz.equals(that.clazz);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clazz);
  }

  @Override
  public <T extends StarboxCommandContext<T, ?>> Pair<Object, Integer> process(
      @NonNull ProvidersRegistry<T> registry,
      @NonNull MessagesProvider<T> messages,
      @NonNull T context,
      int lastIndex)
      throws ArgumentProviderException, MissingArgumentException {
    return new Pair<>(registry.getObject(this.getClazz(), context), 0);
  }
}
