package io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels.management;

import io.github.hyscript7.scriptutils.domain.discord.commands.*;
import net.dv8tion.jda.api.entities.channel.Channel;

public class DeleteChannelSubcommand extends Subcommand {
    public DeleteChannelSubcommand() {
        super(CommandMeta.builder().name("delete").description("Deletes a channel")
                .addOption("channel", "The channel to delete", OptionMeta.Type.CHANNEL, true)
                .build());
    }

    @Override
    public void execute(CommandContext context) {
        Channel channel = (Channel) context.getOption("channel");

        GuildContext guild;
        if (context.getGuild().isEmpty()) {
            context.send("This command can only be ran in a guild!", true);
            return;
        }
        guild = context.getGuild().get();

        context.defer(true);

        ChannelContext channelContext = guild.getChannel(channel.getIdLong()).orElse(null);
        if (channelContext == null) {
            context.send("Channel not found in this guild!", true);
            return;
        }

        String channelName = channelContext.getChannelName();
        long channelId = channelContext.getChannelId();

        channelContext.delete();

        context.send("Channel `" + channelName + "` (ID: " + channelId + ") has been deleted!");
    }
}