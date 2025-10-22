package io.github.hyscript7.scriptutils.domain.discord.commands;

import java.util.Map;
import java.util.Optional;

/*
 * This is a wrapper for the discord command context.
 * It outlines what methods we actually want to use from our commands.
 */
public interface CommandContext {
    /**
     * Returns all options resolved from the underlying interaction event.
     * 
     * @return A map of option names mapped to values
     */
    Map<String, Object> getOptions();

    /**
     * Gets an option's value by name
     * 
     * @param name The name of the option
     * @return The option value
     * @throws IllegalArgumentException If the option is not found
     */
    Object getOption(String name) throws IllegalArgumentException;

    /**
     * Get the ID of the user who ran the command
     * 
     * @return The user ID
     */
    long getAuthorId();

    /**
     * Get the ID of the channel the command was run in.
     * 
     * @return The channel ID
     */
    long getChannelId();

    /**
     * Get the ID of the guild the command was run in.
     * If the command was ran in a DM context, this will return an empty Optional.
     * 
     * @return The guild ID
     */
    Optional<Long> getGuildId();

    /**
     * Get the guild context.
     * 
     * @return The guild context if the command was ran in a guild, otherwise an empty Optional
     */
    Optional<GuildContext> getGuild();

    /**
     * Checks if the command was acknowledged. (Deferred or replied to)
     * 
     * @return True if the command was acknowledged
     */
    boolean isAcknowledged();

    /**
     * Defer the reply to the command interaction for when an action might take longer than discord expects it to.
     * 
     * @param ephemeral True if the message should be ephemeral
     */
    void defer(boolean ephemeral);

    default void defer() {
        defer(false);
    }

    /**
     * Sends a reply to the command interaction.
     * 
     * @param message The message to send
     */
    void send(String message);

    /**
     * Sends a reply to the command interaction.
     * 
     * @param message The message to send
     * @param ephemeral True if the message should be ephemeral (won't do anything if the command was already acknowledged)
     */
    void send(String message, boolean ephemeral);

    /**
     * Returns the underlying interaction event object (implementation-specific)
     * 
     * @return The interaction event
     */
    Object getEvent();
}
