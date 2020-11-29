package com.starfishst.jda.utils.embeds;

import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.utils.Chat;
import com.starfishst.jda.utils.message.MessageQuery;
import com.starfishst.jda.utils.message.MessagesFactory;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.commons.builder.Builder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

/** An easy way to send embeds */
public class EmbedQuery implements Builder<MessageEmbed> {

  /** The builder of the embed */
  @NonNull @Delegate @Getter private final EmbedBuilder embed;

  /**
   * Create an instance
   *
   * @param embed the builder of the embed
   */
  public EmbedQuery(@NonNull EmbedBuilder embed) {
    this.embed = embed;
  }

  /**
   * Send the message
   *
   * @param channel the channel to send the message
   * @param success the action to execute after the message is send
   */
  public void send(@NonNull MessageChannel channel, Consumer<Message> success) {
    Chat.send(channel, this.getAsMessageQuery().build(), success);
  }

  /**
   * Send the message
   *
   * @param channel the channel to send the message
   */
  public void send(@NonNull MessageChannel channel) {
    Chat.send(channel, this.getAsMessageQuery().build());
  }

  /**
   * Send the message
   *
   * @param context the context to get the channel from
   * @param success the action to execute after the message is send
   */
  public void send(@NonNull CommandContext context, Consumer<Message> success) {
    Chat.send(context, this.getAsMessageQuery().build(), success);
  }

  /**
   * Send the message
   *
   * @param context the context to get the channel from
   */
  public void send(@NonNull CommandContext context) {
    Chat.send(context, this.getAsMessageQuery().build());
  }

  /**
   * Get as a message query
   *
   * @return the message query
   */
  @NonNull
  public MessageQuery getAsMessageQuery() {
    return MessagesFactory.fromEmbed(this.build());
  }
}
