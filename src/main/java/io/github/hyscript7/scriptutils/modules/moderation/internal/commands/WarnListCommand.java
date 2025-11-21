package io.github.hyscript7.scriptutils.modules.moderation.internal.commands;

import java.time.ZoneOffset;
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
public class WarnListCommand extends Subcommand {
    private final WarningService warningService;

    public WarnListCommand(WarningService warningService) {
        super(new CommandMeta("list", "Lists all warnings for a member.", List.of(
                new OptionMeta("member", "The member to list warnings for", OptionMeta.Type.USER, true),
                new OptionMeta("page", "The page to list", OptionMeta.Type.INTEGER, false, 1))));
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

        boolean own = context.getAuthorId() == user.getIdLong();
        boolean hasModPerms = guild.memberHasPermission(context.getAuthorId(),
                Permission.MODERATE_MEMBERS.getRawValue());

        // Users can view their own warnings.
        if ((!hasModPerms) && (!own)) {
            context.send("You do not have permission to use this command!", true);
            return;
        }

        // TODO: Paginate. Discord has a message limit. If we would hit that, we need to paginate.
        // int page = (Integer) context.getOption("page");

        List<Warning> warnings = warningService.getWarningsForUser(guild.getGuildId(), user.getIdLong());
        StringBuilder builder = new StringBuilder();
        for (Warning warning : warnings) {
            builder.append("\n- #").append(warning.getId()).append(": `").append(warning.getReason()).append("`");
            builder.append(" (Warned ");
            // We don't show who issued the warning if the user doesn't have mod perms. This is to avoid retribution.
            if (hasModPerms) {
                builder.append("by <@").append(warning.getModeratorId()).append("> ");
            }
            builder.append("<t:").append(warning.getDate().toEpochSecond(ZoneOffset.UTC)).append(":R>)");
        }

        context.send("Warnings for " + user.getAsMention() + ":\n" + builder.toString().trim(), true);
    }

}
