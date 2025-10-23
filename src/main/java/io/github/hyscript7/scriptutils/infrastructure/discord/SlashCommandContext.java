package io.github.hyscript7.scriptutils.infrastructure.discord;

import java.util.Map;
import java.util.Optional;

import io.github.hyscript7.scriptutils.domain.discord.commands.ChannelContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Getter
@Slf4j
public class SlashCommandContext implements CommandContext {
    private final SlashCommandInteractionEvent event;
    private final Map<String, Object> options;

    private long authorId;
    private long channelId;
    private Optional<Long> guildId;
    private Optional<GuildContext> guildContext;

    public SlashCommandContext(SlashCommandInteractionEvent event, Map<String, Object> options, long authorId,
            long channelId, Optional<Long> guildId) {
        this.event = event;
        this.options = options;
        this.authorId = authorId;
        this.channelId = channelId;
        this.guildId = guildId;
        if (guildId.isPresent()) {
            guildContext = Optional.of(new SlashGuildContext(event.getJDA(), guildId.get()));
        }
    }

    @Override
    public boolean isAcknowledged() {
        return event.isAcknowledged();
    }

    @Override
    public Optional<Long> getGuildId() {
        return guildId;
    }

    @Override
    public void defer(boolean ephemeral) {
        event.deferReply(ephemeral).queue();
    }

    @Override
    public void send(String message) {
        if (event.isAcknowledged()) {
            event.getHook().sendMessage(message).queue();
        } else {
            event.reply(message).queue();
        }
    }

    @Override
    public Object getOption(String name) throws IllegalArgumentException {
        Object val = this.options.get(name);
        if (val == null)
            throw new IllegalArgumentException("Option " + name + " not found");
        return val;
    }

    @Override
    public void send(String message, boolean ephemeral) {
        if (event.isAcknowledged()) {
            send(message);
        } else {
            event.reply(message).setEphemeral(ephemeral).queue();
        }
    }

    @Override
    public Optional<GuildContext> getGuild() {
        return guildContext;
    }

    @Override
    public ChannelContext getChannel() {
        return new SlashChannelContext(event.getChannel(), guildContext);
    }
}
