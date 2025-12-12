package io.github.hyscript7.scriptutils.domain.discord.commands;

import java.util.List;

public interface RoleContext {
    /**
     * Get the ID of this role.
     * @return The role ID
     */
    long getRoleId();

    /**
     * Get the name of this role
     * @return The name of the role
     */
    String getName();

    /**
     * Change the name of this role
     * @param name The new name
     */
    void setName(String name);

    /**
     * Get the integer value of this role's color
     * @return The color int value (or 0 if colorless)
     */
    int getColor();

    /**
     * Change the color of the role.
     * @param color The int value of the color (or 0 for none)
     */
    void setColor(int color);

    /**
     * Get a list of all member IDs who have this role.
     * @return A list of IDs
     */
    List<Long> getMemberIds();

    /**
     * Get the number representation of this role's permissions
     * @return A long that can be mapped to a permission enum
     */
    long getPermissions();

    /**
     * Get the context of the guild this role belongs to
     * @return A never-null guild context
     */
    GuildContext getGuild();

    /**
     * Add the role to the specified user.
     * @param userId The ID of the user to give the role to
     */
    void addMember(long userId);

    /**
     * Remove the role from the specified user.
     * @param userId The ID of the user to take the role from
     */
    void removeMember(long userId);

    /**
     * Add the role to all the specified users.
     * @param userIds The IDs of the users to give the role to
     */
    void addMembers(Long ...userIds);

    /**
     * Remove the role from the specified users.
     * @param userIds The IDs of all the users to take the role from
     */
    void removeMembers(Long ...userIds);
}
