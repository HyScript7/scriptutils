package io.github.hyscript7.scriptutils.infrastructure.discord.commands;

import java.util.List;

import io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces.ICommandMetaProvider;
import io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces.ISlashCommand;
import io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces.ISubcommand;
import io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces.ISubcommandGroup;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandGroup implements ISlashCommand {
    private ICommandMetaProvider commandMeta;
    private List<ISubcommandGroup> subcommandGroups;
    private List<ISubcommand> subcommands;

    @Override
    public SlashCommandData getSlashCommandData() {
        SlashCommandData data = Commands.slash(commandMeta.getName(), commandMeta.getFullDescription());
        data.addSubcommandGroups(subcommandGroups.stream().map(ISubcommandGroup::getSubcommandGroupData).toList());
        data.addSubcommands(subcommands.stream().map(ISubcommand::getSubcommandData).toList());
        return data;
    }

    public void addSubcommandGroup(ISubcommandGroup subcommandGroup) {
        if (subcommandGroups.contains(subcommandGroup)) {
            return;
        }
        subcommandGroups.add(subcommandGroup);
    }

    public void addSubcommand(ISubcommand subcommand) {
        if (subcommands.contains(subcommand)) {
            return;
        }
        subcommands.add(subcommand);
    }
}
