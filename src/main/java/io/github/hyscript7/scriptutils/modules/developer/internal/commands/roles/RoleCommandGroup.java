package io.github.hyscript7.scriptutils.modules.developer.internal.commands.roles;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandGroup;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;

public class RoleCommandGroup extends CommandGroup {
    public RoleCommandGroup() {
        super(new CommandMeta("role", "Lets you manage roles."));
    }
}
