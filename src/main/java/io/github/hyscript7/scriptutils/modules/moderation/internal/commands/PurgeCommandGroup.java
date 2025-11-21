package io.github.hyscript7.scriptutils.modules.moderation.internal.commands;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandGroup;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;

public class PurgeCommandGroup extends CommandGroup {
    public PurgeCommandGroup() {
        super(new CommandMeta("purge", "Purges messages based on a filter"));
    }
}
