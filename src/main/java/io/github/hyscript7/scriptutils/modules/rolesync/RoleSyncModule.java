package io.github.hyscript7.scriptutils.modules.rolesync;

import io.github.hyscript7.scriptutils.domain.discord.ModuleMeta;

public class RoleSyncModule extends io.github.hyscript7.scriptutils.domain.discord.Module {

    public RoleSyncModule() {
        super(new ModuleMeta("Role Sync", "Allows for synchronizing member roles between multiple guilds."));
    }

}
