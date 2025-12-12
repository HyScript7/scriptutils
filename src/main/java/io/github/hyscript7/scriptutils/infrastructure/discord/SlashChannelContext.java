package io.github.hyscript7.scriptutils.infrastructure.discord;

import java.util.Optional;

import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.jetbrains.annotations.Nullable;

import io.github.hyscript7.scriptutils.domain.discord.commands.ChannelContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildMessageChannel;

public class SlashChannelContext implements ChannelContext {
    private final Channel channel;
    private final Optional<GuildContext> guildContext;

    public SlashChannelContext(Channel channel, Optional<GuildContext> guildContext) {
        this.channel = channel;
        this.guildContext = guildContext;
    }

    @Override
    public long getChannelId() {
        return channel.getIdLong();
    }

    @Override
    public String getChannelName() {
        return channel.getName();
    }

    /**
     * Returns the topic of the channel the command was run in.
     * 
     * @return The topic of the channel if the channel exists in this guild,
     *         otherwise an empty Optional
     */
    @Override
    public @Nullable String getChannelTopic() {
        if (channel instanceof StandardGuildMessageChannel gc) {
            return gc.getTopic();
        } else {
            return null;
        }
    }

    @Override
    public boolean isGuildChannel() {
        return guildContext.isPresent();
    }

    @Override
    public Optional<GuildContext> getGuild() {
        return guildContext;
    }

    @Override
    public void send(String message) {
        switch (channel.getType()) {
            case ChannelType.TEXT, ChannelType.PRIVATE, ChannelType.VOICE:
                ((MessageChannel) channel).sendMessage(message).queue();
                break;
            default:
                throw new UnsupportedOperationException(
                        "Cannot send a message to a channel of type " + channel.getType());
        }
    }

    @Override
    public void setName(String newName) {
        if (channel instanceof GuildMessageChannel gc) {
            gc.getManager().setName(newName).queue();
        } else {
            throw new UnsupportedOperationException(
                    "Cannot rename a channel of type " + channel.getType());
        }
    }

    @Override
    public void setTopic(String newTopic) {
        if (channel instanceof StandardGuildMessageChannel gc) {
            gc.getManager().setTopic(newTopic).queue();
        } else {
            throw new UnsupportedOperationException(
                    "Cannot set the topic of a channel of type " + channel.getType());
        }
    }

    @Override
    public void purgeMessages(long amount) {
        if (channel instanceof StandardGuildMessageChannel gc) {
            gc.getHistory().retrievePast(Math.toIntExact(amount)).onSuccess(gc::purgeMessages).queue();
        } else {
            throw new UnsupportedOperationException(
                    "Cannot rename a channel of type " + channel.getType());
        }
    }

    @Override
    public void delete() {
        if (channel instanceof GuildChannel gc) {
            gc.delete().queue();
        } else {
            throw new UnsupportedOperationException(
                    "Cannot delete a channel of type " + channel.getType());
        }
    }

}
