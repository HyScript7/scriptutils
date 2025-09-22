package io.github.hyscript7.scriptutils.domain.discord.commands;

import java.util.Optional;

import lombok.Getter;

@Getter
public class OptionMeta {
    public enum Type {
        STRING,
        INTEGER,
        DOUBLE,
        BOOLEAN,
        USER,
        CHANNEL,
        ROLE
    }

    private final String name;
    private final String description;
    private final Type type;
    private final boolean required;
    private final Optional<Object> defaultValue;

    public OptionMeta(String name, String description, Type type, boolean required) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.required = required;
        this.defaultValue = Optional.empty();
    }

    public OptionMeta(String name, String description, Type type, boolean required, Object defaultValue) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.required = required;
        this.defaultValue = Optional.of(defaultValue);
    }
}
