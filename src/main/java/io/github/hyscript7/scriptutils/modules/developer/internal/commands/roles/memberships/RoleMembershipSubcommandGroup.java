package io.github.hyscript7.scriptutils.modules.developer.internal.commands.roles.memberships;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.SubcommandGroup;

public class RoleMembershipSubcommandGroup extends SubcommandGroup {
    public RoleMembershipSubcommandGroup() {
        super(new CommandMeta("members", "Lets you manage role members."));
    }
}
