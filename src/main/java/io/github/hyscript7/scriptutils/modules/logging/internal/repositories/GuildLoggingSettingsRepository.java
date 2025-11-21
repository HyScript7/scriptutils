package io.github.hyscript7.scriptutils.modules.logging.internal.repositories;

import io.github.hyscript7.scriptutils.modules.logging.internal.models.GuildLoggingSettings;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface GuildLoggingSettingsRepository extends CrudRepository<GuildLoggingSettings, Long> {
    Optional<GuildLoggingSettings> findByGuildId(Long guildId);
}