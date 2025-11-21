package io.github.hyscript7.scriptutils.modules.logging.internal.listeners;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.infrastructure.Constants;
import io.github.hyscript7.scriptutils.modules.logging.api.LogAction;
import io.github.hyscript7.scriptutils.modules.logging.api.LogEntry;
import io.github.hyscript7.scriptutils.modules.logging.api.services.DiscordLoggingService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateAvatarEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class MemberLoggingListener extends ListenerAdapter {

    private final DiscordLoggingService discordLoggingService;

    public MemberLoggingListener(DiscordLoggingService discordLoggingService) {
        this.discordLoggingService = discordLoggingService;
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Member Joined");
        embed.addField("Username", event.getUser().getName(), true);
        embed.addField("ID", event.getMember().getId(), true);
        embed.addField("Created", "<t:" + event.getMember().getTimeCreated().toEpochSecond() + ">", true);
        embed.addField("Joined", "<t:" + event.getMember().getTimeJoined().toEpochSecond() + ":R>", true);
        embed.setThumbnail(event.getMember().getAvatarUrl());
        embed.setColor(Constants.Colors.GREEN.getValue()); // Green
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.MEMBER_JOINED, embed.build()));
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Member Left");
        embed.addField("Username", event.getUser().getName(), true);
        embed.addField("ID", event.getUser().getId(), true);
        embed.addField("Created", "<t:" + event.getUser().getTimeCreated().toEpochSecond() + ">", true);
        embed.setThumbnail(event.getUser().getAvatarUrl());
        embed.setColor(Constants.Colors.RED.getValue()); // Red
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.MEMBER_LEFT, embed.build()));
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        List<String> newRolesIDs = event.getRoles().stream().map(r -> r.getId()).collect(Collectors.toList());
        StringBuilder roleMentionsBuilder = new StringBuilder();
        for (String roleID : newRolesIDs) {
            roleMentionsBuilder.append("\n<@&").append(roleID).append(">");
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Roles Added to Member");
        embed.addField("Username", event.getUser().getName(), true);
        embed.addField("ID", event.getUser().getId(), true);
        embed.addField("New Roles", roleMentionsBuilder.toString().trim(), false);
        embed.setThumbnail(event.getUser().getAvatarUrl());
        embed.setColor(Constants.Colors.GREEN.getValue()); // Green
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.MEMBER_ROLE_ADDED, embed.build()));
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        List<String> removedRolesIDs = event.getRoles().stream().map(r -> r.getId()).collect(Collectors.toList());
        StringBuilder roleMentionsBuilder = new StringBuilder();
        for (String roleID : removedRolesIDs) {
            roleMentionsBuilder.append("\n<@&").append(roleID).append(">");
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Roles Removed from Member");
        embed.addField("Username", event.getUser().getName(), true);
        embed.addField("ID", event.getUser().getId(), true);
        embed.addField("Removed Roles", roleMentionsBuilder.toString().trim(), false);
        embed.setThumbnail(event.getUser().getAvatarUrl());
        embed.setColor(Constants.Colors.RED.getValue()); // Green
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.MEMBER_ROLE_REMOVED, embed.build()));
    }

    @Override
    public void onGuildMemberUpdateAvatar(GuildMemberUpdateAvatarEvent event) {
        String newAvatarUrl = event.getNewAvatarUrl();
        String oldAvatarUrl = event.getOldAvatarUrl();

        newAvatarUrl = newAvatarUrl == null ? "None" : newAvatarUrl;
        oldAvatarUrl = oldAvatarUrl == null ? "None" : oldAvatarUrl;

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Member Avatar Updated");
        embed.addField("Username", event.getUser().getName(), true);
        embed.addField("ID", event.getUser().getId(), true);
        embed.addField("New Avatar", newAvatarUrl, true);
        embed.addField("Old Avatar", oldAvatarUrl, true);
        embed.setThumbnail(event.getUser().getAvatarUrl());
        embed.setColor(Constants.Colors.YELLOW.getValue()); // Green
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.MEMBER_AVATAR_CHANGED, embed.build()));


    }

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        String newNickname = event.getNewNickname();
        String oldNickname = event.getOldNickname();

        newNickname = newNickname == null ? event.getUser().getEffectiveName() : newNickname;
        oldNickname = oldNickname == null ? event.getUser().getEffectiveName() : oldNickname;

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Member Nickname Updated");
        embed.addField("Username", event.getUser().getName(), true);
        embed.addField("ID", event.getUser().getId(), true);
        embed.addField("New Nickname", newNickname, true);
        embed.addField("Old Nickname", oldNickname, true);
        embed.setThumbnail(event.getUser().getAvatarUrl());
        embed.setColor(Constants.Colors.YELLOW.getValue()); // Green
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.MEMBER_NICKNAME_CHANGED, embed.build()));
    }

}
