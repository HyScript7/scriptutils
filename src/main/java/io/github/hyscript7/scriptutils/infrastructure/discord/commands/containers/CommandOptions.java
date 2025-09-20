package io.github.hyscript7.scriptutils.infrastructure.discord.commands.containers;

import java.util.ArrayList;
import java.util.List;

import io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces.ICommandOptionProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Getter
@Builder
@AllArgsConstructor
public class CommandOptions implements ICommandOptionProvider {
    // If a list of options is not provided to the builder, an empty java.util.ArrayList will be used.
    @Builder.Default
    private final List<OptionData> options = new ArrayList<>();
}
