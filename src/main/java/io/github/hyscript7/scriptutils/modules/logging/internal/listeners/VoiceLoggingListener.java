package io.github.hyscript7.scriptutils.modules.logging.internal.listeners;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.modules.logging.api.LogAction;
import io.github.hyscript7.scriptutils.modules.logging.api.LogEntry;
import io.github.hyscript7.scriptutils.modules.logging.api.services.DiscordLoggingService;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class VoiceLoggingListener extends ListenerAdapter {

    private final DiscordLoggingService discordLoggingService;

    public VoiceLoggingListener(DiscordLoggingService discordLoggingService) {
        this.discordLoggingService = discordLoggingService;
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        boolean channelChanged = event.getChannelLeft() != null && event.getChannelJoined() != null;
        boolean channelJoined =  event.getChannelJoined() != null && event.getChannelLeft() == null;
        boolean channelLeft =  event.getChannelLeft() != null && event.getChannelJoined() == null;

        LogAction actionType;

        EmbedBuilder embed = new EmbedBuilder();
        embed.addField("Username", event.getMember().getUser().getName(), false);
        embed.addField("ID", event.getMember().getUser().getId(), false);
        if (channelChanged) {
            embed.setTitle("Voice Channels Changed");
            embed.addField("Old Voice Channel", "<#" + event.getChannelLeft().getId() + ">", false);
            embed.addField("New Voice Channel", "<#" + event.getChannelJoined().getId() + ">", false);
            actionType = LogAction.VOICE_CHANNEL_SWITCHED;
        }  else if (channelJoined) {
            embed.setTitle("Voice Channel Joined");
            embed.addField("Voice Channel", "<#" + event.getChannelJoined().getId() + ">", false);
            actionType = LogAction.VOICE_CHANNEL_JOINED;
        } else if  (channelLeft) {
            embed.setTitle("Voice Channel Left");
            embed.addField("Voice Channel", "<#" + event.getChannelLeft().getId() + ">", false);
            actionType = LogAction.VOICE_CHANNEL_LEFT;
        } else {
            // Should never happen... only here to silence the linter.
            return;
        }

        discordLoggingService.log(new LogEntry(event.getJDA(), event.getGuild().getIdLong(), actionType, embed.build()));
    }
}
