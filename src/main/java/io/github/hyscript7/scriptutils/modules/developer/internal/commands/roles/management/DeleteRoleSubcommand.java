package io.github.hyscript7.scriptutils.modules.developer.internal.commands.roles.management;

import io.github.hyscript7.scriptutils.domain.discord.commands.*;
import net.dv8tion.jda.api.entities.Role;

public class DeleteRoleSubcommand extends Subcommand {
    public DeleteRoleSubcommand() {
        super(CommandMeta.builder().name("delete").description("Deletes a role")
                .addOption("role", "The role to delete", OptionMeta.Type.ROLE, true)
                .build());
    }

    @Override
    public void execute(CommandContext context) {
        Role role = (Role) context.getOption("role");

        GuildContext guild;
        if (context.getGuild().isEmpty()) {
            context.send("This command can only be ran in a guild!", true);
            return;
        }
        guild = context.getGuild().get();

        context.defer(true);

        String roleName = role.getName();
        long roleId = role.getIdLong();

        role.delete().queue(
                success -> context.send("Role `" + roleName + "` (ID: " + roleId + ") has been deleted!"),
                error -> context.send("Failed to delete role: " + error.getMessage())
        );
    }
}
