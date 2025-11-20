package io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.groups;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.SubcommandGroup;

@Component
public class RoleSyncGroupSubcommandGroup extends SubcommandGroup {
    
    public RoleSyncGroupSubcommandGroup() {
        super(new CommandMeta("group", "Manage role-sync groups."));
    }
}
