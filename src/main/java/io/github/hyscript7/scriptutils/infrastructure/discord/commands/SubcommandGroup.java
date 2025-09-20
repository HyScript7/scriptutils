package io.github.hyscript7.scriptutils.infrastructure.discord.commands;

import java.util.List;

import io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces.ICommandMetaProvider;
import io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces.ISubcommand;
import io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces.ISubcommandGroup;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public class SubcommandGroup implements ISubcommandGroup {
    private ICommandMetaProvider commandMeta;
    private List<ISubcommand> subcommands;

    @Override
    public SubcommandGroupData getSubcommandGroupData() {
        SubcommandGroupData data = new SubcommandGroupData(commandMeta.getName(), commandMeta.getFullDescription());
        data.addSubcommands(subcommands.stream().map(ISubcommand::getSubcommandData).toList());
        return data;
    }

    public void addSubcommand(ISubcommand subcommand) {
        if (subcommands.contains(subcommand)) {
            return;
        }
        subcommands.add(subcommand);
    }

}
