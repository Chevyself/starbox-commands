package com.github.chevyself.starbox.jda.commands;

import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.jda.context.CommandContext;
import java.util.Optional;
import lombok.NonNull;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

/** Extension of {@link StarboxCommand} for JDA. */
public interface JdaCommand extends StarboxCommand<CommandContext, JdaCommand> {

  /**
   * Get the description of the command.
   *
   * @return the description
   */
  @NonNull
  String getDescription();

  /**
   * Create the command data to use slash commands.
   *
   * @return the command data
   */
  @NonNull
  default SlashCommandData toCommandData() {
    SlashCommandData commandData = Commands.slash(this.getName(), this.getDescription());
    this.getChildren().stream()
        .map(JdaCommand::toSubcommandData)
        .forEach(commandData::addSubcommands);
    return commandData;
  }

  /**
   * Create the command data to use the subcommand feature of slash commands.
   *
   * @return the subcommand data
   */
  @NonNull
  default SubcommandData toSubcommandData() {
    return new SubcommandData(this.getName(), this.getDescription());
  }

  /**
   * Get the id assigned from discord.
   *
   * @return the id or empty if not assigned
   */
  @NonNull
  Optional<String> getId();

  /**
   * Set the id assigned from discord.
   *
   * @param id the id to set
   */
  void setId(String id);
}
