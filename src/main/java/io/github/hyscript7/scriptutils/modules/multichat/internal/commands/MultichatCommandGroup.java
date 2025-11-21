package io.github.hyscript7.scriptutils.modules.multichat.internal.commands;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandGroup;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;

public class MultichatCommandGroup extends CommandGroup {

    public MultichatCommandGroup() {
        super(new CommandMeta("multichat", "Allows for managing cross-server channel links."));
    }
    
}
