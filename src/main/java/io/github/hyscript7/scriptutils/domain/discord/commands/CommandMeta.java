package io.github.hyscript7.scriptutils.domain.discord.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

@Getter
public class CommandMeta {
    private final String name;
    private final String description;
    private final List<OptionMeta> options;

    public CommandMeta(String name, String description) {
        this.name = name.toLowerCase(); // Wow side effects in the constructor. This totally won't bite me later.
        this.description = description;
        this.options = new ArrayList<>();
    }

    public CommandMeta(String name, String description, List<OptionMeta> options) {
        this.name = name.toLowerCase(); // Same complaint as above... I'm such a great programmer.
        this.description = description;
        this.options = options; // This could be abused to modify options at runtime, but that's not the point
    }

    public List<OptionMeta> getOptions() {
        return Collections.unmodifiableList(options);
    }

    public void addOption(OptionMeta option) {
        options.add(option);
    }
}
