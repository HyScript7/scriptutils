package io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.bindings;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.SubcommandGroup;

@Component
public class RoleSyncBindingSubcommandGroup extends SubcommandGroup {

    public RoleSyncBindingSubcommandGroup() {
        super(new CommandMeta("binding",
                "Manage role bindings. (Which role gets synced to which roles on other guilds)"));
    }
}
