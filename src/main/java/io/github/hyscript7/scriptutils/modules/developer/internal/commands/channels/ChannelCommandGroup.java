package io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandGroup;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;

public class ChannelCommandGroup extends CommandGroup {

    public ChannelCommandGroup() {
        super(new CommandMeta("channel", "Lets you manage channels."));
    }
}
