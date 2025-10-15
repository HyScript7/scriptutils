package io.github.hyscript7.scriptutils.infrastructure.discord;

import jakarta.annotation.Nonnull;

import org.springframework.stereotype.Service;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandNode;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Service
@Slf4j
public class CommandHandler extends ListenerAdapter {

    private final CommandRegistry commandRegistry;
    private SlashCommandEventToContextAdapter contextAdapter;

    public CommandHandler(SlashCommandEventToContextAdapter contextAdapter, CommandRegistry commandRegistry) {
        this.contextAdapter = contextAdapter;
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("Received slash command interaction: {}", event.getFullCommandName());
        // When I wrote this, I and God knew what was going on... now only God does
        commandRegistry.getCommand(event.getFullCommandName())
                .ifPresentOrElse(
                        command -> command
                                .execute(contextAdapter.adaptEventToContext(event, ((CommandNode) command).getMeta())),
                        () -> log.warn("Command {} not found, despite being on remote.", event.getFullCommandName()));
    }

}
