package io.github.hyscript7.scriptutils.infrastructure.discord.commands.interfaces;

import java.util.Optional;

public interface ICommandMetaProvider {
    public String getName();

    public String getDescription();

    public Optional<String> getUsage();

    public String getFullDescription();
}
