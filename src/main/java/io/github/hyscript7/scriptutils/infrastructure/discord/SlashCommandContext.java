package io.github.hyscript7.scriptutils.infrastructure.discord;

import java.util.Map;
import java.util.Optional;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Getter
@AllArgsConstructor
@Slf4j
public class SlashCommandContext implements CommandContext {
    private final SlashCommandInteractionEvent event;
    private final Map<String, Object> options;

    long authorId;
    long channelId;
    Optional<Long> guildId;

    @Override
    public boolean isAcknowledged() {
        return event.isAcknowledged();
    }

    @Override
    public Optional<Long> getGuildId() {
        throw new UnsupportedOperationException("Unimplemented method 'getGuildId'");
    }

    @Override
    public void send(String message) {
        log.info("Sending message: {} (acknowledged: {})", message, event.isAcknowledged());
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
        log.info("Sending message with ephemeral param set to {}: {} (acknowledged: {})", ephemeral, message, event.isAcknowledged());
        if (event.isAcknowledged()) {
            send(message);
        } else {
            event.reply(message).setEphemeral(ephemeral).queue();
        }
    }
}
