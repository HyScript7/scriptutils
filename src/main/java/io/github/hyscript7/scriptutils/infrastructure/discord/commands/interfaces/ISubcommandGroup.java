package io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public interface ISubcommandGroup {
    SubcommandGroupData getSubcommandGroupData();

    void addSubcommand(ISubcommand subcommand);
}
