package io.github.hyscript7.scriptutils.domain.discord;

import java.util.ArrayList;
import java.util.List;

import io.github.hyscript7.scriptutils.domain.discord.commands.Command;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandGroup;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandNode;
import lombok.Getter;

@Getter
public class Module {
    private final ModuleMeta meta;
    private final List<CommandNode> commands;
    private final List<Object> eventListeners;

    public Module(ModuleMeta meta) {
        this.meta = meta;
        this.commands = new ArrayList<>();
        this.eventListeners = new ArrayList<>();
    }

    public Module addCommand(CommandNode command) {
        if (commands.contains(command)) {
            throw new IllegalArgumentException(
                    "Command " + command.getName() + " already exists in module's commands.");
        }
        switch (command) {
            case Command c -> commands.add(c);
            case CommandGroup g -> commands.add(g);
            default -> throw new IllegalArgumentException(
                    "Invalid command type: " + command.getClass().getName()
                            + ". The module only accepts root commands (Command and CommandGroup).");
        }
        return this;
    }

    public Module removeCommand(CommandNode command) {
        if (!commands.contains(command)) {
            throw new IllegalArgumentException("Command " + command.getName() + " not found in module's commands.");
        }
        commands.remove(command);
        return this;
    }

    public Module addEventListener(Object listener) {
        if (eventListeners.contains(listener)) {
            throw new IllegalArgumentException("Listener " + listener + " already exists in module's listeners.");
        }
        eventListeners.add(listener);
        return this;
    }

    public Module removeEventListener(Object listener) {
        if (!eventListeners.contains(listener)) {
            throw new IllegalArgumentException("Listener " + listener + " not found in module's listeners.");
        }
        eventListeners.remove(listener);
        return this;
    }
}
