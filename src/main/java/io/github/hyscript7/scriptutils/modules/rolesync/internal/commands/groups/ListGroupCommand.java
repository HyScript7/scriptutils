package io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.groups;

import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncRoleGroup;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.services.RoleSyncGroupService;

import java.util.List;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;

@Component
public class ListGroupCommand extends Subcommand {

    private final RoleSyncGroupService roleSyncGroupService;

    public ListGroupCommand(RoleSyncGroupService roleSyncGroupService) {
        super(new CommandMeta("list", "List all groups you created."));
        this.roleSyncGroupService = roleSyncGroupService;
    }

    @Override
    public void execute(CommandContext context) {
        List<RoleSyncRoleGroup> groups = roleSyncGroupService.getGroupsByOwnerId(context.getAuthorId());
        if (groups.isEmpty()) {
            context.send("You have not created any groups yet.");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder("You have created the following groups:\n");
        groups.forEach(group -> stringBuilder.append("- ").append(group.getName()).append(" - ")
                .append(group.getDescription()).append("\n"));

        context.send(stringBuilder.toString().trim());
    }

}
