package io.github.hyscript7.scriptutils.modules.welcomer;

import io.github.hyscript7.scriptutils.domain.discord.Module;
import io.github.hyscript7.scriptutils.domain.discord.ModuleMeta;

public class WelcomerModule extends Module {
    
    public WelcomerModule() {
        super(new ModuleMeta("Welcomer", "Sends welcome messages to new members when they join the server."));
    }
}
