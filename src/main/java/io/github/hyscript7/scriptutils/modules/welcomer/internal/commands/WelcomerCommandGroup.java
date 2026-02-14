package io.github.hyscript7.scriptutils.modules.welcomer.internal.commands;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandGroup;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;

@Component
public class WelcomerCommandGroup extends CommandGroup {
    
    public WelcomerCommandGroup() {
        super(new CommandMeta("welcomer", "Manage the welcomer module for new members."));
    }
}
