package io.github.hyscript7.scriptutils.domain.discord.commands;

import java.time.Duration;
import java.util.Optional;

public interface GuildContext {
    /**
     * Get the ID of the guild the context belongs to.
     * 
     * @return The guild ID
     */
    long getGuildId();

    /**
     * Timeout a user from the server.
     * 
     * @param userId   the ID of the user to timeout
     * @param duration the duration of the timeout
     * @param reason   the reason for the timeout
     */
    void timeout(long userId, Duration duration, String reason);

    /**
     * Timeout a user from the server.
     * 
     * @param userId   the ID of the user to timeout
     * @param duration the duration of the timeout
     */
    void timeout(long userId, Duration duration);

    /**
     * Kick a user from the server.
     * 
     * @param userId the ID of the user to kick
     * @param reason the reason for kicking the user
     */
    void kick(long userId, String reason);

    /**
     * Kick a user from the server without providing a reason.
     * 
     * @param userId the ID of the user to kick
     */
    void kick(long userId);

    /**
     * Ban a user from the server.
     * 
     * @param userId        the ID of the user to ban
     * @param purgeDuration the amount of days to purge the user's messages
     * @param reason        the reason for banning the user
     */
    void ban(long userId, Duration purgeDuration, String reason);

    /**
     * Ban a user from the server without providing a reason.
     * 
     * @param userId        the ID of the user to ban
     * @param purgeDuration the amount of days to purge the user's messages
     */
    void ban(long userId, Duration purgeDuration);

    /**
     * Ban a user from the server without providing a reason or purging messages.
     * 
     * @param userId the ID of the user to ban
     * @param reason the reason for banning the user
     */
    void ban(long userId, String reason);

    /**
     * Ban a user from the server without providing a reason or purging messages.
     * 
     * @param userId the ID of the user to ban
     */
    void ban(long userId);

    /**
     * Unban a user from the server.
     * 
     * @param userId the ID of the user to unban
     */
    void unban(long userId);

    /**
     * Checks if a member has a permission on the server.
     * 
     * @param userId     the ID of the member
     * @param permission the numeric value of the permissions to check
     * @return true if the member has the permission
     */
    boolean memberHasPermission(long userId, long permissions);

    /**
     * Get the context for a channel.
     * 
     * @param channelId the ID of the channel
     * @return The channel context if the channel exists in this guild, otherwise an
     *         empty Optional
     */
    Optional<ChannelContext> getChannel(long channelId);
}
