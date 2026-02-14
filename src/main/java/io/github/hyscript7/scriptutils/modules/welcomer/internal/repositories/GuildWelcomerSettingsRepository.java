package io.github.hyscript7.scriptutils.modules.welcomer.internal.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.github.hyscript7.scriptutils.modules.welcomer.internal.models.GuildWelcomerSettings;

public interface GuildWelcomerSettingsRepository extends CrudRepository<GuildWelcomerSettings, Long> {
    Optional<GuildWelcomerSettings> findByGuildId(Long guildId);
}
