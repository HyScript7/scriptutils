package io.github.hyscript7.scriptutils.domain.discord.commands;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A subcommand group can only have subcommands.
 */
public class SubcommandGroup extends CommandNode {

    private final Map<String, Subcommand> subcommands = new LinkedHashMap<>();

    public SubcommandGroup(CommandMeta meta) {
        super(meta);
    }

    public void addSubcommand(Subcommand subcommand) {
        subcommands.put(subcommand.getName(), subcommand);
    }

    public Subcommand getSubcommand(String name) {
        return subcommands.get(name);
    }

    public Map<String, Subcommand> getSubcommands() {
        return subcommands;
    }
}
