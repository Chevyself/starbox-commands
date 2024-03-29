package com.github.chevyself.starbox.jda;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.ReflectCommand;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.arguments.SingleArgument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.exceptions.MissingArgumentException;
import com.github.chevyself.starbox.exceptions.type.StarboxException;
import com.github.chevyself.starbox.exceptions.type.StarboxRuntimeException;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.jda.annotations.Command;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.cooldown.CooldownManager;
import com.github.chevyself.starbox.jda.result.JdaResult;
import com.github.chevyself.starbox.jda.result.JdaResultBuilder;
import com.github.chevyself.starbox.jda.result.Result;
import com.github.chevyself.starbox.jda.result.ResultType;
import com.github.chevyself.starbox.messages.StarboxMessagesProvider;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

/**
 * This is the direct extension of {@link JdaCommand} for reflection commands. This is returned from
 * {@link CommandManager#parseCommands(Object)}
 *
 * <p>The methods that are annotated with {@link Command} represent of this commands
 */
public class AnnotatedCommand extends JdaCommand
    implements ReflectCommand<CommandContext, JdaCommand> {

  @NonNull @Getter private final List<JdaCommand> children = new ArrayList<>();
  @NonNull @Getter private final Method method;
  @NonNull @Getter private final Object object;
  @NonNull @Getter private final List<Argument<?>> arguments;
  @NonNull @Getter private final List<String> aliases;

  /**
   * Create a command.
   *
   * @param manager the manager that parsed the command
   * @param description a short description of the command
   * @param map a map that contains custom settings of the command
   * @param options the flags that apply to this command
   * @param middlewares the middlewares to run before and after this command is executed
   * @param cooldown the manager that handles the cooldown in this command
   * @param aliases the names that the command can be executed with
   * @param method the method to execute as the command see more in {@link #getMethod()}
   * @param object the instance of the object used to invoke the method see more in {@link
   *     #getObject()}
   * @param arguments the list of arguments that are used to {@link
   *     #getObjects(StarboxCommandContext)} and invoke the {@link #getMethod()}
   */
  public AnnotatedCommand(
      @NonNull CommandManager manager,
      @NonNull String description,
      @NonNull Map<String, String> map,
      @NonNull List<Option> options,
      @NonNull List<Middleware<CommandContext>> middlewares,
      CooldownManager cooldown,
      @NonNull List<String> aliases,
      @NonNull Method method,
      @NonNull Object object,
      @NonNull List<Argument<?>> arguments) {
    super(manager, description, map, options, middlewares, cooldown);
    this.method = method;
    this.object = object;
    this.arguments = arguments;
    this.aliases = aliases;
  }

  private static OptionData toOptionData(@NonNull Argument<?> argument) {
    if (argument instanceof SingleArgument) {
      return new OptionData(
          AnnotatedCommand.toOptionType(argument.getClazz()),
          ((SingleArgument<?>) argument).getName(),
          ((SingleArgument<?>) argument).getDescription(),
          ((SingleArgument<?>) argument).isRequired());
    }
    return null;
  }

  @NonNull
  private static OptionType toOptionType(@NonNull Class<?> clazz) {
    if (clazz.isAssignableFrom(Number.class)) {
      return OptionType.INTEGER;
    } else if (clazz == boolean.class || clazz == Boolean.class) {
      return OptionType.BOOLEAN;
    } else if (clazz.isAssignableFrom(User.class) || clazz.isAssignableFrom(Member.class)) {
      return OptionType.USER;
    } else if (clazz.isAssignableFrom(MessageChannel.class)) {
      return OptionType.CHANNEL;
    } else if (clazz.isAssignableFrom(Role.class)) {
      return OptionType.ROLE;
    }
    return OptionType.STRING;
  }

  @Override
  public @NonNull ProvidersRegistry<CommandContext> getProvidersRegistry() {
    return this.manager.getProvidersRegistry();
  }

  @Override
  public @NonNull StarboxMessagesProvider<CommandContext> getMessagesProvider() {
    return this.manager.getMessagesProvider();
  }

  @Override
  public JdaResult run(@NonNull CommandContext context) {
    try {
      Object[] objects = this.getObjects(context);
      Object object = this.method.invoke(this.object, objects);
      JdaResult result = null;
      if (object instanceof Result) {
        result = (Result) object;
      } else if (object instanceof JdaResultBuilder) {
        result = ((JdaResultBuilder) object).build();
      }
      return result;
    } catch (final IllegalAccessException e) {
      e.printStackTrace();
      return Result.forType(ResultType.UNKNOWN).setDescription("IllegalAccessException, e").build();
    } catch (final InvocationTargetException e) {
      final String message = e.getTargetException().getMessage();
      if (message != null && !message.isEmpty()) {
        if (!(e.getTargetException() instanceof StarboxException)
            | !(e.getTargetException() instanceof StarboxRuntimeException)) {
          e.printStackTrace();
        }
        return Result.forType(ResultType.ERROR).setDescription(message).build();
      } else {
        e.printStackTrace();
        return Result.forType(ResultType.UNKNOWN)
            .setDescription("InvocationTargetException, e")
            .build();
      }
    } catch (MissingArgumentException e) {
      return Result.forType(ResultType.USAGE).setDescription(e.getMessage()).build();
    } catch (ArgumentProviderException e) {
      return Result.forType(ResultType.ERROR).setDescription(e.getMessage()).build();
    }
  }

  @Override
  public @NonNull SlashCommandData getCommandData() {
    SlashCommandData data = super.getCommandData();
    this.arguments.stream()
        .map(AnnotatedCommand::toOptionData)
        .filter(Objects::nonNull)
        .forEach(data::addOptions);
    return data;
  }
}
