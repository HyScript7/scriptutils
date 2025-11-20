package io.github.hyscript7.scriptutils.modules.rolesync.internal.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncRoleBinding;

public interface RoleSyncRoleBindingRepository extends CrudRepository<RoleSyncRoleBinding, Long> {
    Optional<RoleSyncRoleBinding> findByGuildIdAndRoleId(Long guildId, Long roleId);
    List<RoleSyncRoleBinding> findByGuildId(Long guildId);
}
