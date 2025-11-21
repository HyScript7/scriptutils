package io.github.hyscript7.scriptutils.modules.moderation.internal.commands;

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
public class KickCommand extends Subcommand {

    public KickCommand() {
        super(new CommandMeta("kick", "Kicks a member from the server.",
                List.of(new OptionMeta("member", "The member to kick", OptionMeta.Type.USER, true),
                        new OptionMeta("reason", "The reason for the kick", OptionMeta.Type.STRING, false,
                                Constants.DEFAULT_REASON))));
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
        try {
            guild.kick(user.getIdLong(), reason);
            context.send(
                    "Successfully kicked " + user.getAsMention() + " from the server.\n## Reason\n```\n" + reason + "\n```",
                    true);
        } catch (Exception e) {
            context.send("Failed to kick " + user.getAsMention() + " from the server.\nIs the user a member of the server and are my permissions setup correctly?", true);
        }
    }

}
