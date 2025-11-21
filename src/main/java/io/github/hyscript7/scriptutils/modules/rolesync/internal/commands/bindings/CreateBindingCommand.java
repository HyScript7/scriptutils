package io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.bindings;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncGroupRole;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncRoleGroup;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.services.RoleSyncGroupService;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.services.RoleSyncRoleService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;

@Component
public class CreateBindingCommand extends Subcommand {

    private final RoleSyncGroupService roleSyncGroupService;

    private final RoleSyncRoleService roleSyncRoleService;

    public CreateBindingCommand(RoleSyncRoleService roleSyncRoleService, RoleSyncGroupService roleSyncGroupService) {
        super(new CommandMeta("create", "Creates a new role binding.", List.of(
                new OptionMeta("local", "Mention of the local role to bind", OptionMeta.Type.ROLE, true),
                new OptionMeta("role", "The name of the group's role to bind", OptionMeta.Type.STRING, true),
                new OptionMeta("group", "The name of the group to link to", OptionMeta.Type.STRING, true))));
        this.roleSyncRoleService = roleSyncRoleService;
        this.roleSyncGroupService = roleSyncGroupService;
    }

    @Override
    public void execute(CommandContext context) {
        Optional<GuildContext> guildContext = context.getGuild();
        if (guildContext.isEmpty()) {
            context.send("This command can only be ran in a guild.");
            return;
        }

        if (!guildContext.get().memberHasPermission(context.getAuthorId(), Permission.MANAGE_SERVER.getRawValue() | Permission.MANAGE_ROLES.getRawValue())) {
            context.send("You must have the `MANAGE SERVER` and `MANAGE ROLES` permission to use this command.");
            return;
        }

        Role role = (Role) context.getOption("local");
        String groupName = (String) context.getOption("group");
        String roleName = (String) context.getOption("role");

        Optional<RoleSyncRoleGroup> group = roleSyncGroupService.getGroupByNameAndOwnerId(groupName,
                context.getAuthorId());

        if (group.isEmpty()) {
            context.send("A group with the name `" + groupName
                    + "` does not exist. Did you type the name right? Try `/rolesync group list`.");
            return;
        }

        Optional<RoleSyncGroupRole> roleToBind = roleSyncRoleService.getGroupRoleByNameAndGroup(roleName, group.get());

        if (roleToBind.isEmpty()) {
            context.send("A role with the name `" + roleName
                    + "` does not exist. Did you type the name right? Try `/rolesync group roles`.");
            return;
        }

        roleSyncRoleService.addRoleToGroup(roleToBind.get(), role.getGuild().getIdLong(), role.getIdLong());

        context.send("Created a new role binding for <@&" + role.getIdLong() + "> to `" + groupName + "`'s `" + roleName
                + "` role.");
    }

}
