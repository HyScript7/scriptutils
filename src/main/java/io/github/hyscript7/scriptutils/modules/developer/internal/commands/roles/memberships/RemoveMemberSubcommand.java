package io.github.hyscript7.scriptutils.modules.developer.internal.commands.roles.memberships;

import io.github.hyscript7.scriptutils.domain.discord.commands.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;

public class RemoveMemberSubcommand extends Subcommand {
    public RemoveMemberSubcommand() {
        super(CommandMeta.builder().name("remove").description("Removes a role from a member")
                .addOption("role", "The role to remove", OptionMeta.Type.ROLE, true)
                .addOption("member", "The member to remove the role from", OptionMeta.Type.USER, true)
                .build());
    }

    @Override
    public void execute(CommandContext context) {
        Role role = (Role) context.getOption("role");
        User user = (User) context.getOption("member");

        GuildContext guild = context.getGuild().orElse(null);
        if (guild == null) {
            context.send("This command can only be ran in a guild!", true);
            return;
        }

        if(!guild.memberHasPermission(context.getAuthorId(),
                Permission.MANAGE_ROLES.getRawValue())) {
            context.send("You do not have permission to use this command!", true);
            return;
        }

        context.defer(true);

        role.getGuild().removeRoleFromMember(UserSnowflake.fromId(user.getIdLong()), role).queue(
                success -> context.send("Removed role <@&" + role.getIdLong() + "> from <@" + user.getIdLong() + ">!"),
                error -> context.send("Failed to remove role: " + error.getMessage())
        );
    }
}