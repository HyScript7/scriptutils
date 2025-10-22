package io.github.hyscript7.scriptutils.modules.moderation.internal.commands;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.SubcommandGroup;

@Component
public class NoteSubcommandGroup extends SubcommandGroup {

    public NoteSubcommandGroup() {
        super(new CommandMeta("note", "Manages mod notes on members."));
    }
}
