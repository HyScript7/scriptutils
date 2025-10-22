package io.github.hyscript7.scriptutils.modules.moderation.internal.commands;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;
import io.github.hyscript7.scriptutils.infrastructure.Constants;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

public class TimeoutCommand extends Subcommand {

    public TimeoutCommand() {
        super(new CommandMeta("timeout", "Times out a member for a specified duration.",
                List.of(new OptionMeta("member", "The member to timeout", OptionMeta.Type.USER, true),
                        new OptionMeta("reason", "The reason for the timeout", OptionMeta.Type.STRING, false,
                                Constants.DEFAULT_REASON),
                        new OptionMeta("duration", "How long to timeout the user for (defaults to 2 hours) in ISO8601 format",
                                OptionMeta.Type.STRING, false, "PT2H"))));
    }

    @Override
    public void execute(CommandContext context) {
        Optional<GuildContext> guildOptional = context.getGuild();
        if (guildOptional.isEmpty()) {
            context.send("You must be in a guild to use this command!", true);
            return;
        }
        GuildContext guild = guildOptional.get();

        if (!guild.memberHasPermission(context.getAuthorId(), Permission.KICK_MEMBERS.getRawValue())) {
            context.send("You do not have permission to use this command!", true);
            return;
        }

        User user = (User) context.getOption("member");
        String reason = (String) context.getOption("reason");
        String durationString = (String) context.getOption("duration");
        Duration duration;
        try {
            duration = Duration.parse(durationString);
        } catch (DateTimeParseException e) {
            context.send("Invalid duration. Must be in ISO8601 format. (e.g. `PT2H` = 2 hours, `PT1M` = 1 minute, `PT48H` = 2 days)", true);
            return;
        }
        try {
            guild.timeout(user.getIdLong(), duration, reason);
            context.send(
                    "Successfully timed out " + user.getAsMention() + " for " + durationString + ".\n## Reason\n```\n" + reason
                            + "\n```",
                    true);
        } catch (Exception e) {
            context.send("Failed to timeout " + user.getAsMention()
                    + ".\nIs the user a member of the server and are my permissions setup correctly?",
                    true);
        }
    }

}
