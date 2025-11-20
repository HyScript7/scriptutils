package io.github.hyscript7.scriptutils.modules.multichat.internal.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.github.hyscript7.scriptutils.modules.multichat.internal.models.MultichatGroup;

public interface MultichatGroupRepository extends CrudRepository<MultichatGroup, Long> {
    Optional<MultichatGroup> findByNameAndOwnerId(String name, Long ownerId);
}
