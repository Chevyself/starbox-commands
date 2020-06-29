package com.starfishst.commands.utils.responsive;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class ResponsiveMessage {

    /**
     * The id of this message
     */
    private final long id;

    /**
     * The set of reactions to which this message will respond
     */
    @NotNull private final Set<ReactionResponse> reactions;

    /**
     * Create the responsive message. This constructor should be used when creating the responsive message from a configuration
     *
     * @param id the id of the responsive message
     * @param reactions the reactions to add in the message
     */
    public ResponsiveMessage(long id, @NotNull Set<ReactionResponse> reactions) {
        this.id = id;
        this.reactions = reactions;
    }

    /**
     * Create the responsive message. This constructor should be used in a message that was already sent
     *
     * @param message the message to make the responsive message
     * @param reactions the reactions to use
     */
    public ResponsiveMessage(@NotNull Message message, @NotNull Set<ReactionResponse> reactions) {
        this(message.getIdLong(), reactions);
        this.reactions.forEach(reaction -> {
            if (!reaction.getUnicode().equalsIgnoreCase("any")) {
                message.addReaction(reaction.getUnicode()).queue();
            }
        });
    }

    /**
     * Get the reactions of the message matching an unicode
     *
     * @return the reactions
     * @param unicode the unicode to match
     */
    @NotNull
    public Set<ReactionResponse> getReactions(@NotNull String unicode) {
        return reactions.stream().filter(reaction -> reaction.getUnicode().equalsIgnoreCase("any") || reaction.getUnicode().equalsIgnoreCase(unicode)).collect(Collectors.toSet());
    }

    /**
     * Get the id of the message
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Get the reactions of the message
     *
     * @return the reactions
     */
    @NotNull
    public Set<ReactionResponse> getReactions() {
        return reactions;
    }
}
