package io.github.hyscript7.scriptutils.modules.multichat.internal.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.github.hyscript7.scriptutils.modules.multichat.internal.models.MultichatBinding;

public interface MultichatBindingRepository extends CrudRepository<MultichatBinding, Long> {
    Optional<MultichatBinding> findByChannelId(Long channelId);
    List<MultichatBinding> findByGuildId(Long guildId);
}
