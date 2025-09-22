package io.github.hyscript7.scriptutils.infrastructure.discord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.hyscript7.scriptutils.domain.discord.commands.Command;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandGroup;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandNode;
import io.github.hyscript7.scriptutils.domain.discord.commands.ExecutableCommand;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;
import io.github.hyscript7.scriptutils.domain.discord.commands.SubcommandGroup;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

@Service
public class CommandRegistry {
    private JDACommandTreeAdapter commandTreeAdapter;
    private Map<String, ExecutableCommand> commands;
    private List<CommandNode> rootCommands;

    public CommandRegistry(JDACommandTreeAdapter commandTreeAdapter) {
        this.commandTreeAdapter = commandTreeAdapter;
        this.commands = new HashMap<>();
        this.rootCommands = new ArrayList<>();
    }

    Optional<ExecutableCommand> getCommand(String name) {
        return Optional.ofNullable(commands.get(name));
    }

    public void registerCommand(Command command) {
        if (rootCommands.contains(command)) {
            return;
        }
        commands.put(command.getName(), command);
        rootCommands.add(command);
    }

    public void registerCommand(CommandGroup group) {
        if (rootCommands.contains(group)) {
            return;
        }
        rootCommands.add(group);
        for (CommandNode node : group.getChildren().values()) {
            if (node instanceof Subcommand subcommand) {
                commands.put(
                        composite(Optional.of(group.getName()), Optional.empty(), subcommand.getName()),
                        subcommand);
            } else if (node instanceof SubcommandGroup subcommandGroup) {
                for (Subcommand subcommand : subcommandGroup.getSubcommands().values()) {
                    commands.put(composite(Optional.of(group.getName()), Optional.of(subcommandGroup.getName()),
                            subcommand.getName()), subcommand);
                }
            }
        }
    }

    private String composite(Optional<String> group, Optional<String> subcommandGroup, String subcommand) {
        StringBuilder builder = new StringBuilder();
        group.ifPresent(g -> {
            builder.append(g);
            builder.append(" ");
        });
        subcommandGroup.ifPresent(sg -> {
            builder.append(sg);
            builder.append(" ");
        });
        builder.append(subcommand);
        return builder.toString();
    }

    /**
     * Converts all registered commands to slash command data.
     * 
     * @return A list of slash command data
     */
    public List<SlashCommandData> getSlashCommands() {
        List<SlashCommandData> slashCommands = new ArrayList<>();
        for (CommandNode node : rootCommands) {
            SlashCommandData data;
            switch (node) {
                case Command c -> data = commandTreeAdapter.toSlashCommandData(c);
                case CommandGroup g -> data = commandTreeAdapter.toSlashCommandData(g);
                default -> {
                    continue;
                }
            }
            slashCommands.add(data);
        }
        return slashCommands;
    }

}
