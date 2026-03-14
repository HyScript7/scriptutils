package io.github.hyscript7.scriptutils.modules.moderation.internal.commands;

import java.util.List;
import java.util.Optional;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;
import net.dv8tion.jda.api.Permission;

public class PurgeAllCommand extends Subcommand {

    public PurgeAllCommand() {
        super(new CommandMeta("any", "Purges all messages in the specified range.", List.of(
            new OptionMeta("count", "The number of messages to purge", OptionMeta.Type.INTEGER, true)
        )));
    }

    private static final int PURGE_LIMIT = 100;

    @Override
    public void execute(CommandContext context) {
        Optional<GuildContext> guildOptional = context.getGuild();
        if (guildOptional.isEmpty()) {
            context.send("You must be in a guild to use this command!", true);
            return;
        }
        GuildContext guild = guildOptional.get();

        boolean hasModPerms = guild.memberHasPermission(context.getAuthorId(),
                Permission.MESSAGE_MANAGE.getRawValue());

        if (!hasModPerms) {
            context.send("You do not have permission to use this command!", true);
            return;
        }

        long messageCount = (Long) context.getOption("count");

        boolean warnCapped = messageCount > PURGE_LIMIT;
        messageCount = Math.min(messageCount, PURGE_LIMIT);

        context.send("Purging " + messageCount + " messages" + (warnCapped ? " (capped to 100)" : "") + "...", true);
        context.getChannel().purgeMessages(messageCount);
    }
    
}
