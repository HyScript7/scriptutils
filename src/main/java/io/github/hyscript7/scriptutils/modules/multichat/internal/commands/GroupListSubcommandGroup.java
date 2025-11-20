package io.github.hyscript7.scriptutils.modules.multichat.internal.commands;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.SubcommandGroup;

@Component
public class GroupListSubcommandGroup extends SubcommandGroup {

    public GroupListSubcommandGroup() {
        super(new CommandMeta("list", "Lists all groups."));
    }
    
}
