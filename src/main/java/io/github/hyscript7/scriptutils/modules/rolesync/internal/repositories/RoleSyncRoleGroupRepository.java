package io.github.hyscript7.scriptutils.modules.rolesync.internal.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncRoleGroup;

public interface RoleSyncRoleGroupRepository extends CrudRepository<RoleSyncRoleGroup, Long> {
    Optional<RoleSyncRoleGroup> findByNameAndOwnerId(String name, long ownerId);
    List<RoleSyncRoleGroup> findByOwnerId(long ownerId);
}
