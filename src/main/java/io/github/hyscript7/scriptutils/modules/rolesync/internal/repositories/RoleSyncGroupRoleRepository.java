package io.github.hyscript7.scriptutils.modules.rolesync.internal.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncGroupRole;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncRoleGroup;

public interface RoleSyncGroupRoleRepository extends CrudRepository<RoleSyncGroupRole, Long> {
    Optional<RoleSyncGroupRole> findByNameAndGroup(String name, RoleSyncRoleGroup group);
}
