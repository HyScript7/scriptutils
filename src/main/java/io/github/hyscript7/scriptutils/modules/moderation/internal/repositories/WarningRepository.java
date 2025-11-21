package io.github.hyscript7.scriptutils.modules.moderation.internal.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.github.hyscript7.scriptutils.modules.moderation.internal.models.Warning;

public interface WarningRepository extends CrudRepository<Warning, Long> {
    List<Warning> findByGuildId(Long guildId);

    List<Warning> findByGuildIdAndUserId(Long guildId, Long userId);

    List<Warning> findByGuildIdAndModeratorId(Long guildId, Long moderatorId);

    List<Warning> findByGuildIdAndUserIdAndModeratorId(Long guildId, Long userId, Long moderatorId);

    Optional<Warning> findById(Long id);
}
