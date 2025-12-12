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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;
        private final List<OptionMeta> options = new ArrayList<>();

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder addOption(OptionMeta option) {
            this.options.add(option);
            return this;
        }

        public Builder addOption(String name, String description, OptionMeta.Type type, boolean required) {
            this.options.add(new OptionMeta(name, description, type, required));
            return this;
        }

        public Builder addOption(String name, String description, OptionMeta.Type type, boolean required, Object defaultValue) {
            this.options.add(new OptionMeta(name, description, type, required, defaultValue));
            return this;
        }

        public CommandMeta build() {
            if (name == null || name.isEmpty()) {
                throw new IllegalStateException("Command name cannot be null or empty");
            }
            if (description == null || description.isEmpty()) {
                throw new IllegalStateException("Command description cannot be null or empty");
            }
            return new CommandMeta(name, description, new ArrayList<>(options));
        }
    }
}
