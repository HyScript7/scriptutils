package io.github.hyscript7.scriptutils.modules.logging.internal.listeners;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.infrastructure.Constants;
import io.github.hyscript7.scriptutils.modules.logging.api.LogAction;
import io.github.hyscript7.scriptutils.modules.logging.api.LogEntry;
import io.github.hyscript7.scriptutils.modules.logging.api.services.DiscordLoggingService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateParentEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdatePositionEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkChannelEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateCommunityUpdatesChannelEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateRulesChannelEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateSafetyAlertsChannelEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateSystemChannelEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateHoistedEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateIconEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateMentionableEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePositionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * To any soul unfortunate enough to have to be here, I'm sorry.
 * When I originally wrote this file, I thought it would be an easy
 * task to get all the guild events and simply send them as an event
 * into the logging service. However, I was wrong.
 * 
 * This class is a mess.
 * 
 * Every event, every embed,
 * every field and every value
 * casted to a String is followed
 * by the painful screams of a
 * developer who tried to
 * understand this mess.
 * 
 * Increment timer every time once you are done with this class.
 * Hours wasted here trying to refactor this mess: 4
 * 
 * @author HyScript7 <https://github.com/hyscript7>
 */

@Component
public class ServerLoggingListener extends ListenerAdapter {

    private final DiscordLoggingService discordLoggingService;

    public ServerLoggingListener(DiscordLoggingService discordLoggingService) {
        this.discordLoggingService = discordLoggingService;
    }

    /**
     * GUILD_CHANNEL_CREATED
     * GUILD_CHANNEL_DELETED
     * GUILD_CHANNEL_UPDATED
     */

    @Override
    public void onChannelCreate(ChannelCreateEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Channel Created");
        embed.addField("Name", event.getChannel().getName(), true);
        embed.addField("ID", event.getChannel().getId(), true);
        embed.addField("Reference", "<#" + event.getChannel().getId() + ">", false);
        embed.setColor(Constants.Colors.GREEN.getValue()); // Green
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_CHANNEL_CREATED,
                        embed.build()));
    }

    @Override
    public void onChannelDelete(ChannelDeleteEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Channel Deleted");
        embed.addField("Name", event.getChannel().getName(), true);
        embed.addField("ID", event.getChannel().getId(), true);
        embed.setColor(Constants.Colors.RED.getValue()); // Red
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_CHANNEL_DELETED,
                        embed.build()));
    }

    @Override
    public void onChannelUpdateName(ChannelUpdateNameEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Channel Renamed");
        embed.addField("Name", event.getChannel().getName(), true);
        embed.addField("ID", event.getChannel().getId(), true);
        embed.setColor(Constants.Colors.YELLOW.getValue());
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_CHANNEL_UPDATED,
                        embed.build()));
    }

    @Override
    public void onChannelUpdatePosition(ChannelUpdatePositionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Channel Moved");
        embed.addField("Name", event.getChannel().getName(), true);
        embed.addField("ID", event.getChannel().getId(), true);
        embed.addField("Old Position", String.valueOf(event.getOldValue()), true);
        embed.addField("New Position", String.valueOf(event.getNewValue()), true);
        embed.setColor(Constants.Colors.YELLOW.getValue());
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_CHANNEL_UPDATED,
                        embed.build()));
    }

    @Override
    public void onChannelUpdateParent(ChannelUpdateParentEvent event) {
        String oldParentName = event.getOldValue() == null ? "None" : event.getOldValue().getName();
        String newParentName = event.getNewValue() == null ? "None" : event.getNewValue().getName();
        String oldParentId = event.getOldValue() == null ? "None" : event.getOldValue().getId();
        String newParentId = event.getNewValue() == null ? "None" : event.getNewValue().getId();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Channel Moved");
        embed.addField("Name", event.getChannel().getName(), true);
        embed.addField("ID", event.getChannel().getId(), true);
        embed.addField("Old Parent", oldParentName, true);
        embed.addField("Old Parent ID", oldParentId, true);
        embed.addField("New Parent", newParentName, true);
        embed.addField("New Parent ID", newParentId, true);
        embed.setColor(Constants.Colors.YELLOW.getValue());
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_CHANNEL_UPDATED,
                        embed.build()));

    }

    @Override
    public void onGuildUpdateAfkChannel(GuildUpdateAfkChannelEvent event) {
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_CHANNEL_UPDATED,
                        createEmbedForChannelChange(event.getOldAfkChannel(), event.getNewAfkChannel(), "AFK")
                                .build()));
    }

    @Override
    public void onGuildUpdateCommunityUpdatesChannel(GuildUpdateCommunityUpdatesChannelEvent event) {

        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_CHANNEL_UPDATED,
                        createEmbedForChannelChange(event.getOldCommunityUpdatesChannel(),
                                event.getNewCommunityUpdatesChannel(), "Community Updates").build()));

    }

    @Override
    public void onGuildUpdateRulesChannel(GuildUpdateRulesChannelEvent event) {

        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_CHANNEL_UPDATED,
                        createEmbedForChannelChange(event.getOldRulesChannel(), event.getNewRulesChannel(), "Rules")
                                .build()));
    }

    @Override
    public void onGuildUpdateSafetyAlertsChannel(GuildUpdateSafetyAlertsChannelEvent event) {
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_CHANNEL_UPDATED,
                        createEmbedForChannelChange(event.getOldSafetyAlertsChannel(),
                                event.getNewSafetyAlertsChannel(), "Safety Alerts").build()));
    }

    @Override
    public void onGuildUpdateSystemChannel(GuildUpdateSystemChannelEvent event) {
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_CHANNEL_UPDATED,
                        createEmbedForChannelChange(event.getOldSystemChannel(), event.getNewSystemChannel(), "System")
                                .build()));
    }

    private EmbedBuilder createEmbedForChannelChange(GuildChannel oldChannel, GuildChannel newChannel,
            String specialRoleName) {
        boolean changed = oldChannel != null && newChannel != null;
        boolean removed = oldChannel != null && newChannel == null;
        boolean added = newChannel != null && oldChannel == null;

        EmbedBuilder embed = new EmbedBuilder();
        if (changed && newChannel != null) {
            embed.setTitle(specialRoleName + " Channel Updated");
            embed.addField("Name", newChannel.getName(), true);
            embed.addField("ID", newChannel.getId(), true);
            embed.addField("Reference", "<#" + newChannel.getId() + ">", false);
            embed.setColor(Constants.Colors.YELLOW.getValue());
        } else if (removed) {
            embed.setTitle(specialRoleName + " Channel Removed");
            embed.setColor(Constants.Colors.RED.getValue());
        } else if (added && newChannel != null) {
            embed.setTitle(specialRoleName + " Channel Added");
            embed.addField("Name", newChannel.getName(), true);
            embed.addField("ID", newChannel.getId(), true);
            embed.addField("Reference", "<#" + newChannel.getId() + ">", false);
            embed.setColor(Constants.Colors.GREEN.getValue());
        }
        return embed;
    }

    /**
     * GUILD_ROLE_CREATED
     * GUILD_ROLE_DELETED
     * GUILD_ROLE_UPDATED
     */

    @Override
    public void onRoleCreate(RoleCreateEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Role Created");
        embed.addField("Name", event.getRole().getName(), true);
        embed.addField("ID", event.getRole().getId(), true);
        embed.addField("Reference", "<@&" + event.getRole().getId() + ">", false);
        embed.setColor(Constants.Colors.GREEN.getValue());
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_ROLE_CREATED,
                        embed.build()));
    }

    @Override
    public void onRoleDelete(RoleDeleteEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Role Deleted");
        embed.addField("Name", event.getRole().getName(), true);
        embed.addField("ID", event.getRole().getId(), true);
        embed.setColor(Constants.Colors.RED.getValue());
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_ROLE_DELETED,
                        embed.build()));
    }

    @Override
    public void onRoleUpdateColor(RoleUpdateColorEvent event) {
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_ROLE_UPDATED,
                        createRoleChangedEmbed(event.getRole(),
                                Map.of("Old Color", String.valueOf(event.getOldColorRaw()), "New Color",
                                        String.valueOf(event.getNewColorRaw())))
                                .build()));
    }

    @Override
    public void onRoleUpdateHoisted(RoleUpdateHoistedEvent event) {
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_ROLE_UPDATED,
                        createRoleChangedEmbed(event.getRole(),
                                Map.of("Display On Member List", event.getNewValue() ? "Yes" : "No")).build()));
    }

    @Override
    public void onRoleUpdateIcon(RoleUpdateIconEvent event) {
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_ROLE_UPDATED,
                        createRoleChangedEmbed(event.getRole(), Map.of("Old Icon",
                                event.getOldIcon() != null ? event.getOldIcon().getIconUrl() : "None", "New Icon",
                                event.getNewIcon() != null ? event.getNewIcon().getIconUrl() : "None")).build()));
    }

    @Override
    public void onRoleUpdateMentionable(RoleUpdateMentionableEvent event) {
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_ROLE_UPDATED,
                        createRoleChangedEmbed(event.getRole(),
                                Map.of("Mentionable", event.getNewValue() ? "Yes" : "No")).build()));
    }

    @Override
    public void onRoleUpdateName(RoleUpdateNameEvent event) {
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_ROLE_UPDATED,
                        createRoleChangedEmbed(event.getRole(),
                                Map.of("Old Name", event.getOldName(), "New Name", event.getNewName())).build()));
    }

    @Override
    public void onRoleUpdatePermissions(RoleUpdatePermissionsEvent event) {
        EnumSet<Permission> newPerms = event.getNewPermissions();
        EnumSet<Permission> oldPerms = event.getOldPermissions();
        List<Permission> added = newPerms.stream().filter(newPerm -> !oldPerms.contains(newPerm)).toList();
        List<Permission> removed = oldPerms.stream().filter(oldPerm -> !newPerms.contains(oldPerm)).toList();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Role Permissions Updated");
        embed.addField("Name", event.getRole().getName(), true);
        embed.addField("ID", event.getRole().getId(), true);
        embed.addField("Reference", "<@&" + event.getRole().getId() + ">", false);
        embed.setColor(Constants.Colors.YELLOW.getValue());
        if (!added.isEmpty()) {
            embed.addField("Added Permissions", String.join("\n- ", added.stream().map(Permission::getName).toList()),
                    false);
        } else {
            embed.addField("Added Permissions", "None", false);
        }
        if (!removed.isEmpty()) {
            embed.addField("Removed Permissions",
                    String.join("\n- ", removed.stream().map(Permission::getName).toList()), false);
        } else {
            embed.addField("Removed Permissions", "None", false);
        }
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_ROLE_UPDATED,
                        embed.build()));

    }

    @Override
    public void onRoleUpdatePosition(RoleUpdatePositionEvent event) {
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_ROLE_UPDATED,
                        createRoleChangedEmbed(event.getRole(),
                                Map.of("Old Position", String.valueOf(event.getOldPosition()), "New Position",
                                        String.valueOf(event.getNewPosition())))
                                .build()));
    }

    private EmbedBuilder createRoleChangedEmbed(Role role, Map<String, String> changes) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Role Updated");
        embed.addField("Name", role.getName(), true);
        embed.addField("ID", role.getId(), true);
        embed.addField("Reference", "<@&" + role.getId() + ">", false);
        embed.setColor(Constants.Colors.YELLOW.getValue());
        for (Map.Entry<String, String> entry : changes.entrySet()) {
            embed.addField(entry.getKey(), entry.getValue(), true);
        }
        return embed;
    }

    /**
     * GUILD_OWNERSHIP_TRANSFER
     */

    @Override
    public void onGuildUpdateOwner(GuildUpdateOwnerEvent event) {
        String oldOwnerName, oldOwnerId, newOwnerName, newOwnerId;
        if (event.getOldOwner() == null) {
            oldOwnerName = "None";
            oldOwnerId = "None";
        } else {
            oldOwnerName = event.getOldOwner().getUser().getName();
            oldOwnerId = event.getOldOwner().getUser().getId();
        }
        if (event.getNewOwner() == null) {
            newOwnerName = "None";
            newOwnerId = "None";
        } else {
            newOwnerName = event.getNewOwner().getUser().getName();
            newOwnerId = event.getNewOwner().getUser().getId();
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Guild Ownership Transferred");
        embed.addField("Old Owner", oldOwnerName, true);
        embed.addField("Old Owner ID", oldOwnerId, true);
        if (!oldOwnerId.equals("None")) {
            embed.addField("Old Owner Mention", "<@" + oldOwnerId + ">", true);
        }
        embed.addField("New Owner", newOwnerName, true);
        embed.addField("New Owner ID", newOwnerId, true);
        if (!newOwnerId.equals("None")) {
            embed.addField("New Owner Mention", "<@" + newOwnerId + ">", true);
        }
        embed.setColor(Constants.Colors.YELLOW.getValue());
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.GUILD_OWNERSHIP_TRANSFER,
                        embed.build()));
    }

}
