package io.github.hyscript7.scriptutils.modules.moderation.internal.commands;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;
import io.github.hyscript7.scriptutils.infrastructure.Constants;
import io.github.hyscript7.scriptutils.modules.moderation.internal.models.Warning;
import io.github.hyscript7.scriptutils.modules.moderation.internal.services.WarningService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

@Component
public class WarnAddCommand extends Subcommand {
    private final WarningService warningService;

    public WarnAddCommand(WarningService warningService) {
        super(new CommandMeta("add", "Warns a member.",
                List.of(new OptionMeta("member", "The member to warn", OptionMeta.Type.USER, true),
                        new OptionMeta("reason", "The reason for the warning", OptionMeta.Type.STRING, false,
                                Constants.DEFAULT_REASON))));
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

        boolean hasModPerms = guild.memberHasPermission(context.getAuthorId(),
                Permission.MODERATE_MEMBERS.getRawValue());

        if (!hasModPerms) {
            context.send("You do not have permission to use this command!", true);
            return;
        }

        User user = (User) context.getOption("member");
        String reason = (String) context.getOption("reason");

        Warning warning = warningService.issueNewWarning(guild.getGuildId(), user.getIdLong(), context.getAuthorId(),
                reason,
                LocalDateTime.now(ZoneOffset.UTC));

        context.send("Successfully warned " + user.getAsMention() + " for `" + warning.getReason() + "`", true);

        // TODO: Dm the user, otherwise post in current channel
    }

}
