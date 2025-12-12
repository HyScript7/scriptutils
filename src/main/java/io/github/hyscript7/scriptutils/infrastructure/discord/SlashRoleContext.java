package io.github.hyscript7.scriptutils.infrastructure.discord;

import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.RoleContext;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;

import java.util.List;

public class SlashRoleContext implements RoleContext {
    private final Role role;
    private final GuildContext guildContext;

    public SlashRoleContext(Role role, GuildContext guildContext) {
        this.role = role;
        this.guildContext = guildContext;
    }

    @Override
    public long getRoleId() {
        return role.getIdLong();
    }

    @Override
    public String getName() {
        return role.getName();
    }

    @Override
    public void setName(String name) {
        role.getManager().setName(name).queue();
    }

    @Override
    public int getColor() {
        return role.getColorRaw();
    }

    @Override
    public void setColor(int color) {
        role.getManager().setColor(color).queue();
    }

    @Override
    public List<Long> getMemberIds() {
        return role.getGuild().getMembersWithRoles(role).stream().map(ISnowflake::getIdLong).toList();
    }

    @Override
    public long getPermissions() {
        return role.getPermissionsRaw();
    }

    @Override
    public GuildContext getGuild() {
        return guildContext;
    }

    @Override
    public void addMember(long userId) {
        role.getGuild().addRoleToMember(UserSnowflake.fromId(userId), role).queue();
    }

    @Override
    public void removeMember(long userId) {
        role.getGuild().removeRoleFromMember(UserSnowflake.fromId(userId), role).queue();
    }

    @Override
    public void addMembers(Long... userIds) {
        // There should be a REST action to do this in bulk, but it seems JDA doesn't have that atm?
        // Or maybe I'm just blind.
        for (long userId : userIds) {
            addMember(userId);
        }
    }

    @Override
    public void removeMembers(Long... userIds) {
        // Same thing here as with addMembers(userIds)
        for (long userId : userIds) {
            removeMember(userId);
        }
    }
}
