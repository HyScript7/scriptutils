package io.github.hyscript7.scriptutils.domain.discord.commands;

import java.util.Optional;

/**
 * This is a wrapper for the discord channel context.
 * It outlines what methods we actually want to use from our commands.
 */
public interface ChannelContext {
    /**
     * Returns the ID of the channel the command was run in.
     * 
     * @return The channel ID
     */
    long getChannelId();

    /**
     * Returns the name of the channel the command was run in.
     * 
     * @return The channel name
     */
    String getChannelName();

    /**
     * Returns the topic of the channel the command was run in.
     * 
     * @return The channel topic
     */
    String getChannelTopic();

    /**
     * Checks if the channel is a guild channel.
     * 
     * @return True if the channel is a guild, false otherwise
     */
    boolean isGuildChannel();

    /**
     * Get the guild context.
     * 
     * @return The guild context if the command was ran in a guild, otherwise an
     *         empty Optional
     */
    Optional<GuildContext> getGuild();

    /**
     * Sends a reply to the command interaction.
     * 
     * @param message The message to send
     */
    void send(String message);

    /**
     * Sets a new name for the channel.
     * 
     * @param newName The new name for the channel
     */
    void setName(String newName);

    /**
     * Sets a new topic for the channel.
     * 
     * @param newTopic The new topic for the channel
     */
    void setTopic(String newTopic);

    /**
     * Purges messages from the channel.
     * 
     * @param amount The amount of messages to purge
     */
    void purgeMessages(long amount);

    /**
     * Deletes the channel.
     */
    void delete();
}
