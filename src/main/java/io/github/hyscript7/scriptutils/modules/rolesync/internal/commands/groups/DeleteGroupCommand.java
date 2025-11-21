package io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.groups;

import java.util.List;
import java.util.Optional;

import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncRoleGroup;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.services.RoleSyncGroupService;
import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;

@Component
public class DeleteGroupCommand extends Subcommand {

    private final RoleSyncGroupService roleSyncGroupService;

    public DeleteGroupCommand(RoleSyncGroupService roleSyncGroupService) {
        super(new CommandMeta("delete", "Deletes a group of roles.", List.of(
            new OptionMeta("name", "The name of the group to delete.", OptionMeta.Type.STRING, true)
        )));
        this.roleSyncGroupService = roleSyncGroupService;
    }

    @Override
    public void execute(CommandContext context) {
        String name = (String) context.getOption("name");

        Optional<RoleSyncRoleGroup> group = roleSyncGroupService.getGroupByNameAndOwnerId(name, context.getAuthorId());
        if (group.isEmpty()) {
            context.send("A group with the name `" + name + "` does not exist. Did you type the name right? Try `/rolesync group list`.");
            return;
        }

        roleSyncGroupService.deleteGroup(group.get());
        context.send("Deleted group `" + name + "`.");
    }
    
}
