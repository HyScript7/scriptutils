package io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.groups;

import java.util.List;
import java.util.Optional;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.services.RoleSyncGroupService;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncGroupRole;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncRoleGroup;

@Component
public class ListRolesCommand extends Subcommand {

    private final RoleSyncGroupService roleSyncGroupService;

    public ListRolesCommand(RoleSyncGroupService roleSyncGroupService) {
        super(new CommandMeta("roles", "Lists all roles in a group.", List.of(
                new OptionMeta("group", "Name of the group", OptionMeta.Type.STRING, true))));
        this.roleSyncGroupService = roleSyncGroupService;
    }

    @Override
    @Transactional
    public void execute(CommandContext context) {
        String groupName = (String) context.getOption("group");

        Optional<RoleSyncRoleGroup> group = roleSyncGroupService.getGroupByNameAndOwnerId(groupName,
                context.getAuthorId());

        if (group.isEmpty()) {
            context.send("A group with the name `" + groupName
                    + "` does not exist. Did you type the name right? Try `/rolesync group list`.");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder("Group `" + groupName + "` has the following roles:\n");
        List<RoleSyncGroupRole> roles = group.get().getRoles();

        if (roles.isEmpty()) {
            stringBuilder.append("None.");
            context.send(stringBuilder.toString());
            return;
        }

        roles.forEach(role -> stringBuilder.append("- ").append(role.getName()).append(" - ")
                .append(role.getDescription()).append("\n"));
        context.send(stringBuilder.toString().trim());
    }

}
