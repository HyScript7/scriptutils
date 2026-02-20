package io.github.hyscript7.scriptutils.modules.logging.internal.commands;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandGroup;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import org.springframework.stereotype.Component;

@Component
public class LoggingCommandGroup extends CommandGroup {
    public LoggingCommandGroup() {
        super(new CommandMeta("logging", "Commands for modifying and viewing logging configuration"));
    }
}
