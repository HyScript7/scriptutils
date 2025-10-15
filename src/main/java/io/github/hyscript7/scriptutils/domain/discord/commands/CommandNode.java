package io.github.hyscript7.scriptutils.domain.discord.commands;

import lombok.Getter;

// Shared properties for all members of the command tree (top level commands, command groups, subcommands, subcommand groups)

@Getter
public abstract class CommandNode {
    private final CommandMeta meta;

    protected CommandNode(CommandMeta meta) {
        this.meta = meta;
    }

    public String getName() {
        return meta.getName();
    }

    public String getDescription() {
        return meta.getDescription();
    }
}
