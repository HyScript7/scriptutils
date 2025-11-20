package io.github.hyscript7.scriptutils.modules.rolesync.internal.services;

import io.github.hyscript7.scriptutils.modules.rolesync.internal.repositories.RoleSyncGroupRoleRepository;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.repositories.RoleSyncRoleBindingRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncGroupRole;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncRoleBinding;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncRoleGroup;

@Service
public class RoleSyncRoleService {

    private final RoleSyncRoleBindingRepository roleSyncRoleBindingRepository;

    private final RoleSyncGroupRoleRepository roleSyncGroupRoleRepository;

    RoleSyncRoleService(RoleSyncGroupRoleRepository roleSyncGroupRoleRepository,
            RoleSyncRoleBindingRepository roleSyncRoleBindingRepository) {
        this.roleSyncGroupRoleRepository = roleSyncGroupRoleRepository;
        this.roleSyncRoleBindingRepository = roleSyncRoleBindingRepository;
    }

    public RoleSyncGroupRole createGroupRole(String name, String description, RoleSyncRoleGroup group) {
        RoleSyncGroupRole groupRole = RoleSyncGroupRole.builder().name(name).description(description).group(group)
                .build();
        return roleSyncGroupRoleRepository.save(groupRole);
    }

    public RoleSyncGroupRole updateGroupRole(RoleSyncGroupRole groupRole) {
        return roleSyncGroupRoleRepository.save(groupRole);
    }

    public void deleteGroupRole(RoleSyncGroupRole groupRole) {
        roleSyncGroupRoleRepository.delete(groupRole);
    }

    public Optional<RoleSyncGroupRole> getGroupRoleByNameAndGroup(String name, RoleSyncRoleGroup group) {
        return roleSyncGroupRoleRepository.findByNameAndGroup(name, group);
    }

    public RoleSyncRoleBinding addRoleToGroup(RoleSyncGroupRole groupRole, Long guildId, Long roleId) {
        RoleSyncRoleBinding binding = new RoleSyncRoleBinding();
        binding.setGuildId(guildId);
        binding.setRoleId(roleId);
        binding.setRole(groupRole);
        return roleSyncRoleBindingRepository.save(binding);
    }

    public void removeRoleFromGroup(RoleSyncRoleBinding binding) {
        roleSyncRoleBindingRepository.delete(binding);
    }

    public Optional<RoleSyncRoleBinding> getRoleBinding(Long guildId, Long roleId) {
        return roleSyncRoleBindingRepository.findByGuildIdAndRoleId(guildId, roleId);
    }

    public List<RoleSyncRoleBinding> getRoleBindingsByGuildId(Long guildId) {
        return roleSyncRoleBindingRepository.findByGuildId(guildId);
    }

}
