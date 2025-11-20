package io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.bindings;

import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncRoleBinding;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.services.RoleSyncRoleService;
import net.dv8tion.jda.api.Permission;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;

@Component
public class ListBindingCommand extends Subcommand {

    private final RoleSyncRoleService roleSyncRoleService;

    public ListBindingCommand(RoleSyncRoleService roleSyncRoleService) {
        super(new CommandMeta("list", "List all role bindings on this guild."));
        this.roleSyncRoleService = roleSyncRoleService;
    }

    @Override
    public void execute(CommandContext context) {
        Optional<GuildContext> guildContext = context.getGuild();
        if (guildContext.isEmpty()) {
            context.send("You must be in a guild to use this command.");
            return;
        }

        if (!guildContext.get().memberHasPermission(context.getAuthorId(), Permission.MANAGE_SERVER.getRawValue() | Permission.MANAGE_ROLES.getRawValue())) {
            context.send("You must have the `MANAGE SERVER` and `MANAGE ROLES` permission to use this command.");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder("This guild synchronizes the following roles:\n");
        List<RoleSyncRoleBinding> bindings = roleSyncRoleService
                .getRoleBindingsByGuildId(guildContext.get().getGuildId());
        
        if (bindings.isEmpty()) {
            stringBuilder.append("None.");
            context.send(stringBuilder.toString());
            return;
        }

        bindings.forEach(
                binding -> stringBuilder.append("- <@&").append(binding.getRoleId()).append("> via ")
                        .append(binding.getRole().getName()).append(" - ").append(binding.getRole().getDescription())
                        .append("\n"));
        context.send(stringBuilder.toString().trim());
    }

}
