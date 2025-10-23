package io.github.hyscript7.scriptutils.modules.logging.internal.listeners;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.infrastructure.Constants;
import io.github.hyscript7.scriptutils.modules.logging.api.LogAction;
import io.github.hyscript7.scriptutils.modules.logging.api.LogEntry;
import io.github.hyscript7.scriptutils.modules.logging.api.services.DiscordLoggingService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class ModerationLoggingListener extends ListenerAdapter {

    private final DiscordLoggingService discordLoggingService;

    public ModerationLoggingListener(DiscordLoggingService discordLoggingService) {
        this.discordLoggingService = discordLoggingService;
    }

    @Override
    public void onGuildBan(GuildBanEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("User Banned");
        embed.addField("Username", event.getUser().getName(), true);
        embed.addField("ID", event.getUser().getId(), true);
        embed.setThumbnail(event.getUser().getAvatarUrl());
        embed.setColor(Constants.Colors.RED.getValue()); // Red
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.MEMBER_BANNED,
                        embed.build()));
    }

    @Override
    public void onGuildUnban(GuildUnbanEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("User Unbanned");
        embed.addField("Username", event.getUser().getName(), true);
        embed.addField("ID", event.getUser().getId(), true);
        embed.setThumbnail(event.getUser().getAvatarUrl());
        embed.setColor(Constants.Colors.GREEN.getValue()); // Green
        discordLoggingService
                .log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), LogAction.MEMBER_UNBANNED,
                        embed.build()));
    }

}
