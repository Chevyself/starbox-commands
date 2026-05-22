package com.github.chevyself.starbox.jda.commands;

import com.github.chevyself.starbox.commands.AbstractBuiltCommand;
import com.github.chevyself.starbox.commands.CommandBuilder;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.metadata.CommandMetadata;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/** Extension of {@link AbstractBuiltCommand} for JDA. */
@Getter
public class JdaBuiltCommand extends AbstractBuiltCommand<CommandContext, JdaCommand>
    implements JdaCommand {

  @NonNull private final String description;
  @Setter private String id;

  /**
   * Create the command.
   *
   * @param builder the builder
   */
  public JdaBuiltCommand(@NonNull CommandBuilder<CommandContext, JdaCommand> builder) {
    super(builder);
    CommandMetadata metadata = builder.getMetadata();
    this.description = builder.getMetadata().has("description") ? metadata.get("description") : "";
  }

  @Override
  public @NonNull Optional<String> getId() {
    return Optional.ofNullable(this.id);
  }
}
