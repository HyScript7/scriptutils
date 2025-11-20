package io.github.hyscript7.scriptutils.modules.rolesync.internal.services;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.repositories.RoleSyncRoleGroupRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncRoleGroup;

@Service
public class RoleSyncGroupService {

    private final RoleSyncRoleGroupRepository roleSyncRoleGroupRepository;

    public RoleSyncGroupService(RoleSyncRoleGroupRepository roleSyncRoleGroupRepository) {
        this.roleSyncRoleGroupRepository = roleSyncRoleGroupRepository;
    }

    public RoleSyncRoleGroup createGroup(Long ownerId, String name, String description) {
        RoleSyncRoleGroup group = new RoleSyncRoleGroup();
        group.setOwnerId(ownerId);
        group.setName(name);
        group.setDescription(description);
        return roleSyncRoleGroupRepository.save(group);
    }

    public RoleSyncRoleGroup updateGroup(RoleSyncRoleGroup group) {
        return roleSyncRoleGroupRepository.save(group);
    }

    public void deleteGroup(RoleSyncRoleGroup group) {
        roleSyncRoleGroupRepository.delete(group);
    }

    public Optional<RoleSyncRoleGroup> getGroupByNameAndOwnerId(String name, long ownerId) {
        return roleSyncRoleGroupRepository.findByNameAndOwnerId(name, ownerId);
    }

    public List<RoleSyncRoleGroup> getGroupsByOwnerId(long ownerId) {
        return roleSyncRoleGroupRepository.findByOwnerId(ownerId);
    } 
    
}
