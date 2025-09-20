package io.github.hyscript7.scriptutils.infrastructure.discord.commands.containers;

import java.util.Optional;

import io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces.ICommandMetaProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommandMeta implements ICommandMetaProvider {
    private final String name;
    private final String description;
    @Builder.Default
    private final Optional<String> usage = Optional.empty();

    public String getFullDescription() {
        return description + (usage.isPresent() ? "\n\nUsage: " + usage.get() : "");
    }
}
