package io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces;

import java.util.Collection;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public interface ICommandOptionProvider {
    Collection<OptionData> getOptions();
}
