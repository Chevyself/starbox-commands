package com.github.chevyself.starbox.jda.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.arguments.SingleArgument;
import com.github.chevyself.starbox.commands.AbstractAnnotatedCommand;
import com.github.chevyself.starbox.jda.context.CommandContext;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

/** Extension of {@link AbstractAnnotatedCommand} for JDA. */
@Getter
public class JdaAnnotatedCommand extends AbstractAnnotatedCommand<CommandContext, JdaCommand>
    implements JdaCommand {

  @NonNull private final String description;
  @Setter private String id;

  /**
   * Create the command.
   *
   * @param commandManager the command manager
   * @param annotation the annotation
   * @param object the object to invoke
   * @param method the method to invoke
   */
  public JdaAnnotatedCommand(
      @NonNull CommandManager<CommandContext, JdaCommand> commandManager,
      @NonNull Command annotation,
      @NonNull Object object,
      @NonNull Method method) {
    super(commandManager, annotation, object, method);
    this.description = Command.Supplier.getDescription(method);
  }

  private static OptionData toOptionData(@NonNull Argument<?> argument) {
    if (argument instanceof SingleArgument) {
      return new OptionData(
          JdaAnnotatedCommand.toOptionType(argument.getClazz()),
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
  public @NonNull SlashCommandData toCommandData() {
    SlashCommandData data = JdaCommand.super.toCommandData();
    this.arguments.stream()
        .map(JdaAnnotatedCommand::toOptionData)
        .filter(Objects::nonNull)
        .forEach(data::addOptions);
    return data;
  }

  @Override
  public @NonNull Optional<String> getId() {
    return Optional.ofNullable(this.id);
  }
}
