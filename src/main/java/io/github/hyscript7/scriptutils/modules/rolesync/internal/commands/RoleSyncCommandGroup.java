package io.github.hyscript7.scriptutils.modules.rolesync.internal.commands;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandGroup;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;

@Component
public class RoleSyncCommandGroup extends CommandGroup {

    public RoleSyncCommandGroup() {
        super(new CommandMeta("rolesync", "Lets you manage how roles get synced between your servers."));
    }

}
