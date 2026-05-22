package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.exceptions.MissingArgumentException;
import com.github.chevyself.starbox.result.Result;
import com.github.chevyself.starbox.result.type.ArgumentExceptionResult;
import com.github.chevyself.starbox.result.type.InternalExceptionResult;
import com.github.chevyself.starbox.util.Pair;
import java.lang.reflect.Method;
import lombok.NonNull;

/**
 * A reflection command is a command that is parsed using Java reflection. That's why this includes
 * methods as {@link #getMethod} or {@link #getObject}
 *
 * @param <C> the type of context that is required to run the command
 * @param <T> the type of command that can be children
 */
public interface ReflectCommand<
        C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>>
    extends ArgumentedStarboxCommand<C, T> {

  /**
   * Get the objects that should be used in the parameters to invoke {@link #getMethod()}. The
   * parameters are parsed using {@link com.github.chevyself.starbox.parsers.CommandLineParser}
   *
   * @param context the context to get the parameters
   * @return the objects to use as parameters in the {@link #getMethod()}
   * @throws ArgumentProviderException if the argument could not be provided, see {@link
   *     ArgumentProviderException}
   * @throws MissingArgumentException if the command is missing an argument. Also, it will try to
   *     return the result as a help message to get a correct input from the user, see {@link
   *     MissingArgumentException}
   */
  @NonNull
  default Object[] getObjects(C context)
      throws MissingArgumentException, ArgumentProviderException {
    Object[] objects = new Object[this.getArguments().size()];
    int lastIndex = 0;
    for (int i = 0; i < this.getArguments().size(); i++) {
      Pair<Object, Integer> pair =
          this.getArguments()
              .get(i)
              .process(this.getProvidersRegistry(), this.getMessagesProvider(), context, lastIndex);
      objects[i] = pair.getA();
      lastIndex += pair.getB();
    }
    return objects;
  }

  @Override
  default Result run(@NonNull C context) {
    try {
      Object object = this.getMethod().invoke(this.getObject(), this.getObjects(context));
      if (object instanceof Result) {
        return (Result) object;
      } else {
        return null;
      }
    } catch (final MissingArgumentException | ArgumentProviderException e) {
      return new ArgumentExceptionResult(e);
    } catch (final Throwable e) {
      return new InternalExceptionResult(e);
    }
  }

  /**
   * Get the method handle to run a command. This is annotated with the respective @Command
   * annotation, and it is required to call the command
   *
   * @return the method handle to execute a command
   */
  @NonNull
  Method getMethod();

  /**
   * Get the instance of a class that contains a command. It is required to call the {@link
   * Method#invoke(Object, Object...)} because non-static methods cannot be called without it,
   * static methods have no problem
   *
   * @return the class instance of a command method
   */
  @NonNull
  Object getObject();
}
