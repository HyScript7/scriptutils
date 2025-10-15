package io.github.hyscript7.scriptutils.domain.discord;

import lombok.Getter;

@Getter
public class ModuleMeta {
    private final String name;
    private final String description;

    public ModuleMeta(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
