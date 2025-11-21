package io.github.hyscript7.scriptutils.modules.moderation.internal.commands;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;
import io.github.hyscript7.scriptutils.modules.moderation.internal.models.Warning;
import io.github.hyscript7.scriptutils.modules.moderation.internal.services.WarningService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

@Component
public class WarnRevokeCommand extends Subcommand {
    private final WarningService warningService;

    public WarnRevokeCommand(WarningService warningService) {
        super(new CommandMeta("revoke", "Removes a warning from a member.", List.of(
                new OptionMeta("member", "The member to list warnings for", OptionMeta.Type.USER, true),
                new OptionMeta("caseid", "The ID of the warning to revoke", OptionMeta.Type.INTEGER, true))));
        this.warningService = warningService;
    }

    @Override
    public void execute(CommandContext context) {
        Optional<GuildContext> guildOptional = context.getGuild();
        if (guildOptional.isEmpty()) {
            context.send("You must be in a guild to use this command!", true);
            return;
        }
        GuildContext guild = guildOptional.get();

        User user = (User) context.getOption("member");

        boolean hasModPerms = guild.memberHasPermission(context.getAuthorId(),
                Permission.MODERATE_MEMBERS.getRawValue());

        if (!hasModPerms) {
            context.send("You do not have permission to use this command!", true);
            return;
        }

        long caseId = (Long) context.getOption("caseid");

        String notFoundMessage = "Could not find warning with ID " + caseId;

        Optional<Warning> warningOptional = warningService.getWarningById(caseId);
        if (warningOptional.isEmpty()) {
            context.send(notFoundMessage, true);
            return;
        }
        Warning warning = warningOptional.get();
        // If the warning does not belong to the guild the command was run in
        if (warning.getGuildId() != guild.getGuildId()) {
            context.send(notFoundMessage, true);
            return;
        }
        // If the warning does not belong to the user we are trying to revoke the
        // warning from
        if (warning.getUserId() != user.getIdLong()) {
            context.send(notFoundMessage, true);
            return;
        }

        warningService.revokeWarning(warning);
        context.send("Successfully revoked warning `" + warning.getReason() + "`", true);
    }

}
