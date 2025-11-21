package io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.groups;

import java.util.List;
import java.util.Optional;

import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncRoleGroup;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.services.RoleSyncGroupService;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.services.RoleSyncRoleService;
import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;

@Component
public class AddRoleCommand extends Subcommand {

    private final RoleSyncGroupService roleSyncGroupService;

    private final RoleSyncRoleService roleSyncRoleService;
    
    public AddRoleCommand(RoleSyncRoleService roleSyncRoleService, RoleSyncGroupService roleSyncGroupService) {
        super(new CommandMeta("mkrole", "Add a role to a group", List.of(
            new OptionMeta("group", "Name of the group", OptionMeta.Type.STRING, true),
            new OptionMeta("name", "Name of the role", OptionMeta.Type.STRING, true),
            new OptionMeta("description", "Description of the role", OptionMeta.Type.STRING, false)
        )));
        this.roleSyncRoleService = roleSyncRoleService;
        this.roleSyncGroupService = roleSyncGroupService;
    }

    @Override
    public void execute(CommandContext context) {
        String groupName = (String) context.getOption("group");
        String roleName = (String) context.getOption("name");
        String description;
        try {
            description = (String) context.getOption("description");
        } catch (IllegalArgumentException e) {
            description = "No description provided.";
        }

        Optional<RoleSyncRoleGroup> group = roleSyncGroupService.getGroupByNameAndOwnerId(groupName, context.getAuthorId());

        if (group.isEmpty()) {
            context.send("A group with the name `" + groupName + "` does not exist. Did you type the name right? Try `/rolesync group list`.");
            return;
        }

        roleSyncRoleService.createGroupRole(roleName, description, group.get());

        context.send("Role `" + roleName + "` added to group `" + groupName + "` with description `" + description + "`.");
    }
    
}
