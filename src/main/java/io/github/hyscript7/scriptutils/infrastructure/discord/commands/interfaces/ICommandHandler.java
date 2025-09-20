package io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces;

import io.github.hyscript7.scriptutils.infrastructure.discord.commands.exceptions.BaseCommandException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface ICommandHandler {
    /**
     * Handles the execution of a Slash command.
     * This method presumes defer() has already been called.
     * 
     * @param event
     * @throws BaseCommandException
     */
    void execute(SlashCommandInteractionEvent event) throws BaseCommandException;
}
