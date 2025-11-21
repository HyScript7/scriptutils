package io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.bindings;

import java.util.List;
import java.util.Optional;

import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncRoleBinding;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.services.RoleSyncRoleService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;

@Component
public class DeleteBindingCommand extends Subcommand {

    private final RoleSyncRoleService roleSyncRoleService;

    public DeleteBindingCommand(RoleSyncRoleService roleSyncRoleService) {
        super(new CommandMeta("delete", "Delete a role binding.", List.of(
            new OptionMeta("local", "Mention of the local role to unbind", OptionMeta.Type.ROLE, true)
        )));
        this.roleSyncRoleService = roleSyncRoleService;
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

        Optional<RoleSyncRoleBinding> binding = roleSyncRoleService.getRoleBinding(guildContext.get().getGuildId(), role.getIdLong());

        if (binding.isEmpty()) {
            context.send("No role binding found for <@&" + role.getIdLong() + ">.");
            return;
        }

        roleSyncRoleService.removeRoleFromGroup(binding.get());

        context.send("Removed role binding for <@&" + role.getIdLong() + ">.");
    }
    
}
