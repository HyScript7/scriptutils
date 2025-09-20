package io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces;

import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface ISlashCommand {
    SlashCommandData getSlashCommandData();

    void addSubcommandGroup(ISubcommandGroup subcommandGroup);

    void addSubcommand(ISubcommand subcommand);
}
