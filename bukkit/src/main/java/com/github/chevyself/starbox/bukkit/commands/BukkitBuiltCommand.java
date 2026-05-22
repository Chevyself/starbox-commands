package com.github.chevyself.starbox.bukkit.commands;

import com.github.chevyself.starbox.bukkit.BukkitAdapter;
import com.github.chevyself.starbox.bukkit.BukkitCommandExecutor;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.commands.AbstractBuiltCommand;
import com.github.chevyself.starbox.commands.CommandBuilder;
import com.github.chevyself.starbox.metadata.CommandMetadata;
import lombok.Getter;
import lombok.NonNull;

/** Extension of {@link AbstractBuiltCommand} for bukkit commands. */
@Getter
public class BukkitBuiltCommand extends AbstractBuiltCommand<CommandContext, BukkitCommand>
    implements BukkitCommand {

  @NonNull private final BukkitAdapter adapter;
  @NonNull private final BukkitCommandExecutor executor;
  private final String permission;

  /**
   * Create a new built command.
   *
   * @param builder the builder which built this command
   * @param adapter the bukkit adapter
   */
  public BukkitBuiltCommand(
      @NonNull CommandBuilder<CommandContext, BukkitCommand> builder,
      @NonNull BukkitAdapter adapter) {
    super(builder);
    CommandMetadata metadata = builder.getMetadata();
    this.adapter = adapter;
    this.executor =
        new BukkitCommandExecutor(
            this.getName(),
            metadata.has("description") ? metadata.get("description") : "",
            metadata.has("usage") ? metadata.get("usage") : "",
            builder.getAliases(),
            this,
            metadata.has("async") ? metadata.get("async") : false);
    this.permission = metadata.has("permission") ? metadata.get("permission") : null;
  }
}
