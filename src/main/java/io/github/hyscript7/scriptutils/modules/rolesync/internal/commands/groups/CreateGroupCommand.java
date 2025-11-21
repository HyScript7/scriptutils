package io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.groups;

import java.util.List;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.services.RoleSyncGroupService;

@Component
public class CreateGroupCommand extends Subcommand {

    private final RoleSyncGroupService roleSyncGroupService;

    public CreateGroupCommand(RoleSyncGroupService roleSyncGroupService) {
        super(new CommandMeta("create", "Create a new group of roles.", List.of(
            new OptionMeta("name", "The name of the group.", OptionMeta.Type.STRING, true),
            new OptionMeta("description", "The description of the group.", OptionMeta.Type.STRING, false)
        )));
        this.roleSyncGroupService = roleSyncGroupService;
    }

    @Override
    public void execute(CommandContext context) {
        String name = (String) context.getOption("name");
        String description;
        try {
            description = (String) context.getOption("description");
        } catch (IllegalArgumentException e) {
            description = "No description provided.";
        }

        roleSyncGroupService.createGroup(context.getAuthorId(), name, description);

        context.send("Created group `" + name + "` with description `" + description + "`.");
    }
    
}
