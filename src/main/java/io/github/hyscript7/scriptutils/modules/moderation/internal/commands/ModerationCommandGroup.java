package io.github.hyscript7.scriptutils.modules.moderation.internal.commands;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandGroup;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;

@Component
public class ModerationCommandGroup extends CommandGroup {

    public ModerationCommandGroup() {
        super(new CommandMeta("moderation", "TODO"));
    }

}
