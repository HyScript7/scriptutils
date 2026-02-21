package io.github.hyscript7.scriptutils.domain.discord.commands;

import net.dv8tion.jda.api.entities.TeamMember;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.List;
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
     * @param permissions the numeric value of the permissions to check
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

    /**
     * Creates a new channel on this guild and returns its context.
     *
     * @param channelType The type of channel to create
     * @param name The name for the new channel
     * @param categoryId The ID of the category to create the channel in (or null if none)
     * @return The new channels context
     */
    ChannelContext createChannel(ChannelType channelType, String name, @Nullable Long categoryId);
    // TODO: Replace categoryId with CategoryContext or smthing

    /**
     * Creates a new role on this guild and returns its context.
     *
     * @param name The name for the new role
     * @param color The color of the new role
     * @param mentionable Whether the new role should be mentionable by everyone (even without the mention @everyone permission)
     * @param distinct Whether the role should show separately in the member list sidebar
     * @param positionedAfter The ID of the role to position this role after (above) or null if default
     * @return The new roles context
     */
    RoleContext createRole(String name, int color, boolean mentionable, boolean distinct, @Nullable Long positionedAfter);
}
