package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.ReflectCommand;
import com.github.chevyself.starbox.annotations.Parent;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.exceptions.MissingArgumentException;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.messages.StarboxMessagesProvider;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import com.github.chevyself.starbox.system.context.CommandContext;
import com.github.chevyself.starbox.system.context.sender.CommandSender;
import com.github.chevyself.starbox.util.Strings;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

/**
 * This is the direct extension of {@link SystemCommand} for reflection commands this is returned
 * from {@link CommandManager#parseCommands(Object)}.
 *
 * <p>The methods that are annotated with {@link Command} represent of this commands
 */
public class ReflectSystemCommand extends AbstractSystemCommand
    implements ReflectCommand<CommandContext, SystemCommand> {

  @NonNull @Getter private final CommandManager manager;
  @NonNull @Getter private final Method method;
  @NonNull @Getter private final Object object;
  @NonNull @Getter private final List<Argument<?>> arguments;

  /**
   * Create the command.
   *
   * @param manager the manager that parsed the command
   * @param aliases the aliases that match the command for its execution
   * @param options the flags that apply in this command
   * @param middlewares the middlewares to run before and after this command is executed
   * @param method the method to execute as the command see more in {@link #getMethod()}
   * @param object the instance of the object used to invoke the method see more in {@link
   *     #getObject()}
   * @param arguments the list of arguments that are used to {@link
   *     #getObjects(StarboxCommandContext)} and invoke the {@link #getMethod()}
   * @param children the list of children commands which can be used with this parent prefix. Learn
   *     more in {@link Parent}
   * @param cooldown the manager that handles the cooldown in this command
   */
  public ReflectSystemCommand(
      @NonNull CommandManager manager,
      @NonNull List<String> aliases,
      @NonNull List<Option> options,
      @NonNull List<Middleware<CommandContext>> middlewares,
      @NonNull Method method,
      @NonNull Object object,
      @NonNull List<Argument<?>> arguments,
      @NonNull List<SystemCommand> children,
      CooldownManager cooldown) {
    super(aliases, children, options, middlewares, cooldown);
    this.method = method;
    this.object = object;
    this.arguments = arguments;
    this.manager = manager;
  }

  @Override
  public SystemResult execute(@NonNull CommandContext context) {
    return super.execute(context);
  }

  @Override
  public SystemResult run(@NonNull CommandContext context) {
    CommandSender sender = context.getSender();
    try {
      Object object = this.method.invoke(this.getObject(), this.getObjects(context));
      if (object instanceof SystemResult) {
        return (SystemResult) object;
      } else {
        return null;
      }
    } catch (final IllegalAccessException e) {
      e.printStackTrace();
      return new Result("IllegalAccessException: " + e.getMessage() + ", e");
    } catch (final InvocationTargetException e) {
      final String message = e.getMessage();
      if (message != null && !message.isEmpty()) {
        return new Result("{0}");
      } else {
        e.printStackTrace();
        return new Result("InvocationTargetException: " + e.getMessage() + "e");
      }
    } catch (MissingArgumentException | ArgumentProviderException e) {
      return new Result(e.getMessage());
    }
  }

  @Override
  public @NonNull String getUsage() {
    return this.manager.getListener().getPrefix()
        + Strings.buildUsageAliases(this.getAliases())
        + Argument.generateUsage(this.getArguments());
  }

  @Override
  public @NonNull ProvidersRegistry<CommandContext> getProvidersRegistry() {
    return this.getManager().getProvidersRegistry();
  }

  @Override
  public @NonNull StarboxMessagesProvider<CommandContext> getMessagesProvider() {
    return this.getManager().getMessagesProvider();
  }

  @Override
  public boolean hasAlias(@NonNull String alias) {
    for (String name : this.getAliases()) {
      if (name.equalsIgnoreCase(alias)) {
        return true;
      }
    }
    return false;
  }
}
