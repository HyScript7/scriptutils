package io.github.hyscript7.scriptutils.infrastructure.discord.commands;

import io.github.hyscript7.scriptutils.infrastructure.discord.commands.containers.CommandMeta;
import io.github.hyscript7.scriptutils.infrastructure.discord.commands.containers.CommandOptions;
import io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces.ICommandHandler;
import io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces.ICommandMetaProvider;
import io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces.ICommandOptionProvider;
import io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces.ISlashCommand;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

/**
 * A single-level command with options & metadata.
 */
public abstract class Command implements ICommandHandler, ISlashCommand {
    protected final ICommandMetaProvider commandMeta;
    protected final ICommandOptionProvider commandOptions;

    protected Command(CommandMeta commandMeta, CommandOptions commandOptions) {
        this.commandMeta = commandMeta;
        this.commandOptions = commandOptions;
    }

    public SlashCommandData getSlashCommandData() {
        return Commands.slash(commandMeta.getName(), commandMeta.getFullDescription())
                .addOptions(commandOptions.getOptions());
    }
}
