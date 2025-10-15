package io.github.hyscript7.scriptutils.infrastructure.discord;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

/**
 * Yet another wonderful product of DDD - the adapter which converts
 * SlashCommandEvents into CommandContexts.
 * Don't thank me.
 */
@Component
public class SlashCommandEventToContextAdapter {

    @SuppressWarnings("null")
    public CommandContext adaptEventToContext(SlashCommandInteractionEvent event, CommandMeta commandMeta) {
        // If this is getting called, then a theoretical CommandHandler already knows
        // what command this is, so we can depend on a CommandMeta.

        Map<String, Object> options = new HashMap<>();
        for (OptionMeta optMeta : commandMeta.getOptions()) {
            OptionMapping optMapping = event.getOption(optMeta.getName());
            if (optMapping == null) {
                // Throw an error if a required option is missing, otherwise, just skip it, as it's optional
                if (optMeta.isRequired())
                    throw new IllegalArgumentException("Option " + optMeta.getName() + " is required");
                continue;
            }

            Object converted = convertOption(optMapping, optMeta);
            options.put(optMeta.getName(), converted);
        }

        return new SlashCommandContext(
                event, Collections.unmodifiableMap(options),
                event.getInteraction().getUser().getIdLong(),
                event.getChannel().getIdLong(),
                event.getGuild() != null ? Optional.of(event.getGuild().getIdLong()) : Optional.empty());
    }

    private Object convertOption(OptionMapping optMapping, OptionMeta meta) {
        switch (meta.getType()) {
            case STRING:
                return optMapping.getAsString();
            case INTEGER:
                return optMapping.getAsLong();
            case DOUBLE:
                return optMapping.getAsDouble();
            case BOOLEAN:
                return optMapping.getAsBoolean();
            case USER:
                return optMapping.getAsUser();
            case CHANNEL:
                return optMapping.getAsChannel();
            case ROLE:
                return optMapping.getAsRole();
            default:
                throw new IllegalArgumentException("Unsupported option type: " + meta.getType()
                        + ", did you forget to implement a resolver in the context adapter?");
        }
    }
}
