package io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels.permissions;

import io.github.hyscript7.scriptutils.domain.discord.commands.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;

public class ResetPermissionsSubcommand extends Subcommand {
    public ResetPermissionsSubcommand() {
        super(CommandMeta.builder().name("reset").description("Resets all permission overrides for a channel")
                .addOption("channel", "The channel to reset permissions for", OptionMeta.Type.CHANNEL, true)
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

        if(!guild.memberHasPermission(context.getAuthorId(),
                Permission.MANAGE_CHANNEL.getRawValue())) {
            context.send("You do not have permission to use this command!", true);
            return;
        }

        if (!(channel instanceof GuildChannel guildChannel)) {
            context.send("The specified channel is not a guild channel!", true);
            return;
        }

        context.defer(true);

        ChannelContext channelContext = guild.getChannel(channel.getIdLong()).orElse(null);
        if (channelContext == null) {
            context.send("Channel not found in this guild!", true);
            return;
        }

        int overrideCount = guildChannel.getPermissionContainer().getPermissionOverrides().size();

        guildChannel.getPermissionContainer().getPermissionOverrides().forEach(override -> {
            override.delete().queue();
        });

        context.send("Reset all permission overrides (" + overrideCount + " total) for channel <#" + channel.getIdLong() + ">!");
    }
}
