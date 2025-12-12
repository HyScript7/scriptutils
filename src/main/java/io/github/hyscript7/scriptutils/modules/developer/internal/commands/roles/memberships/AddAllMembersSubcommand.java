package io.github.hyscript7.scriptutils.modules.developer.internal.commands.roles.memberships;

import io.github.hyscript7.scriptutils.domain.discord.commands.*;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;

import java.util.List;

public class AddAllMembersSubcommand extends Subcommand {
    public AddAllMembersSubcommand() {
        super(CommandMeta.builder().name("addall").description("Adds a role to all members who have another role")
                .addOption("role", "The role to add", OptionMeta.Type.ROLE, true)
                .addOption("from", "Add to all members who have this role", OptionMeta.Type.ROLE, true)
                .build());
    }

    @Override
    public void execute(CommandContext context) {
        Role roleToAdd = (Role) context.getOption("role");
        Role sourceRole = (Role) context.getOption("from");

        GuildContext guild;
        if (context.getGuild().isEmpty()) {
            context.send("This command can only be ran in a guild!", true);
            return;
        }
        guild = context.getGuild().get();

        if (roleToAdd.getIdLong() == sourceRole.getIdLong()) {
            context.send("The role to add and the source role cannot be the same!", true);
            return;
        }

        context.defer(true);

        List<Long> memberIds = sourceRole.getGuild().getMembersWithRoles(sourceRole).stream()
                .map(member -> member.getIdLong())
                .toList();

        if (memberIds.isEmpty()) {
            context.send("No members found with role <@&" + sourceRole.getIdLong() + ">!");
            return;
        }

        for (Long memberId : memberIds) {
            roleToAdd.getGuild().addRoleToMember(UserSnowflake.fromId(memberId), roleToAdd).queue(
                    success -> {},
                    error -> {}
            );
        }

        context.send("Adding role <@&" + roleToAdd.getIdLong() + "> to " + memberIds.size() +
                " member(s) who have <@&" + sourceRole.getIdLong() + ">!");
    }
}