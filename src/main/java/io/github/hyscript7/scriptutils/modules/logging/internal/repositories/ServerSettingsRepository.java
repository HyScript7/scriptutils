package io.github.hyscript7.scriptutils.modules.logging.internal.repositories;

import io.github.hyscript7.scriptutils.modules.logging.internal.models.ServerSettings;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ServerSettingsRepository extends CrudRepository<ServerSettings, Long> {
    Optional<ServerSettings> findByGuildId(Long guildId);
}