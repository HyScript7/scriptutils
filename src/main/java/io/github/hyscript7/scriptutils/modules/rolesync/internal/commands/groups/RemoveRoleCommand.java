package io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.groups;

import java.util.List;
import java.util.Optional;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.services.RoleSyncGroupService;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.services.RoleSyncRoleService;
import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncGroupRole;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncRoleGroup;

@Component
public class RemoveRoleCommand extends Subcommand {

    private final RoleSyncRoleService roleSyncRoleService;

    private final RoleSyncGroupService roleSyncGroupService;
    
    public RemoveRoleCommand(RoleSyncGroupService roleSyncGroupService, RoleSyncRoleService roleSyncRoleService) {
        super(new CommandMeta("rmrole", "Remove a role from a group", List.of(
            new OptionMeta("group", "Name of the group", OptionMeta.Type.STRING, true),
            new OptionMeta("name", "Name of the role", OptionMeta.Type.STRING, true)
        )));
        this.roleSyncGroupService = roleSyncGroupService;
        this.roleSyncRoleService = roleSyncRoleService;
    }

    @Override
    public void execute(CommandContext context) {
        String groupName = (String) context.getOption("group");
        String roleName = (String) context.getOption("name");

        Optional<RoleSyncRoleGroup> group = roleSyncGroupService.getGroupByNameAndOwnerId(groupName, context.getAuthorId());

        if (group.isEmpty()) {
            context.send("A group with the name `" + groupName + "` does not exist. Did you type the name right? Try `/rolesync group list`.");
            return;
        }

        Optional<RoleSyncGroupRole> role = roleSyncRoleService.getGroupRoleByNameAndGroup(roleName, group.get());

        if (role.isEmpty()) {
            context.send("A role with the name `" + roleName + "` does not exist. Did you type the name right? Try `/rolesync group roles`.");
            return;
        }

        roleSyncRoleService.deleteGroupRole(role.get());

        context.send("Role `" + roleName + "` deleted from group `" + groupName + "`.");
    }
    
}
