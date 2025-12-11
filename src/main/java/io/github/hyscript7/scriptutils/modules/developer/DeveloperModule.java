package io.github.hyscript7.scriptutils.modules.developer;

import io.github.hyscript7.scriptutils.domain.discord.Module;
import io.github.hyscript7.scriptutils.domain.discord.ModuleMeta;

public class DeveloperModule extends Module {

    public DeveloperModule() {
        super(new ModuleMeta("Developer", "Provides utilities for discord server administrators."));
    }
}
