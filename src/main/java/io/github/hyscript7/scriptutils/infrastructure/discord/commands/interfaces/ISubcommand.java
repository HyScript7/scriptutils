package io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public interface ISubcommand extends ICommandHandler {
    SubcommandData getSubcommandData();
}
