package io.github.hyscript7.scriptutils.modules.moderation.internal.commands;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.SubcommandGroup;

@Component
public class WarnSubcommandGroup extends SubcommandGroup {

    public WarnSubcommandGroup() {
        super(new CommandMeta("warn", "Manages member warnings."));
    }

}
