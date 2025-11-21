package io.github.hyscript7.scriptutils.modules.logging;

import io.github.hyscript7.scriptutils.domain.discord.Module;
import io.github.hyscript7.scriptutils.domain.discord.ModuleMeta;

public class LoggingModule extends Module {
    public LoggingModule() {
        super(new ModuleMeta("Logging",
                "Advanced logging features for your server, integrating with other ScriptUtils features."));
    }
}
