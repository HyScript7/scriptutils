package io.github.hyscript7.scriptutils.domain.discord.commands;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A command group can contain either subcommand groups or subcommands,
 * but is not directly executable itself.
 */
public class CommandGroup extends CommandNode {

    private final Map<String, CommandNode> children = new LinkedHashMap<>();

    public CommandGroup(CommandMeta meta) {
        super(meta);
    }

    public void addSubcommandGroup(SubcommandGroup group) {
        children.put(group.getName(), group);
    }

    public void addSubcommand(Subcommand subcommand) {
        children.put(subcommand.getName(), subcommand);
    }

    public CommandNode getChild(String name) {
        return children.get(name);
    }

    public Map<String, CommandNode> getChildren() {
        return children;
    }
}
