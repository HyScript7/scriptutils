package io.github.hyscript7.scriptutils.modules.moderation.internal.commands;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;
import io.github.hyscript7.scriptutils.infrastructure.Constants;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

@Component
public class BanCommand extends Subcommand {

    public BanCommand() {
        super(new CommandMeta("ban", "Bans a member from the server.",
                List.of(new OptionMeta("member", "The member to ban", OptionMeta.Type.USER, true),
                        new OptionMeta("reason", "The reason for the ban", OptionMeta.Type.STRING, false,
                                Constants.DEFAULT_REASON),
                        new OptionMeta("purge", "How many days of the users messages to purge",
                                OptionMeta.Type.INTEGER, false, 7))));
    }

    @Override
    public void execute(CommandContext context) {
        Optional<GuildContext> guildOptional = context.getGuild();
        if (guildOptional.isEmpty()) {
            context.send("You must be in a guild to use this command!", true);
            return;
        }
        GuildContext guild = guildOptional.get();

        if (!guild.memberHasPermission(context.getAuthorId(), Permission.BAN_MEMBERS.getRawValue())) {
            context.send("You do not have permission to use this command!", true);
            return;
        }

        User user = (User) context.getOption("member");
        String reason = (String) context.getOption("reason");
        int purgeDays = (Integer) context.getOption("purge");
        Duration purgeDuration = Duration.ofDays(purgeDays);
        try {
            guild.ban(user.getIdLong(), purgeDuration, reason);
            context.send(
                    "Successfully banned " + user.getAsMention() + ".\n## Reason\n```\n" + reason
                            + "\n```",
                    true);
        } catch (Exception e) {
            context.send("Failed to ban " + user.getAsMention()
                    + ".\nIs the user a member of the server and are my permissions setup correctly?",
                    true);
        }
    }

}
