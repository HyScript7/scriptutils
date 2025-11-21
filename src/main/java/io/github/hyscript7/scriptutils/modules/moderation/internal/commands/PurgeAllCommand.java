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

    @Override
    public void execute(CommandContext context) {
        Optional<GuildContext> guildOptional = context.getGuild();
        if (guildOptional.isEmpty()) {
            context.send("You must be in a guild to use this command!", true);
            return;
        }
        GuildContext guild = guildOptional.get();

        // TODO: This permission might be deprecated soon on discord's side.
        boolean hasModPerms = guild.memberHasPermission(context.getAuthorId(),
                Permission.MESSAGE_MANAGE.getRawValue());

        if (!hasModPerms) {
            context.send("You do not have permission to use this command!", true);
            return;
        }

        long messageCount = (Long) context.getOption("count");

        boolean warnCapped = messageCount > 1000;
        messageCount = Math.min(messageCount, 1000);

        context.send("Purging " + messageCount + " messages" + (warnCapped ? " (capped to 1000)" : "") + "...", true);
        context.getChannel().purgeMessages(messageCount);
    }
    
}
