package io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels.permissions;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.SubcommandGroup;

public class ChannelPermissionSubcommandGroup extends SubcommandGroup {
    public ChannelPermissionSubcommandGroup() {
        super(new CommandMeta("permissions", "Lets you manage channel access"));
    }
}
