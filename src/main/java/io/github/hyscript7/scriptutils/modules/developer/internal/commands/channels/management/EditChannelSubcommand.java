package io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels.management;

import io.github.hyscript7.scriptutils.domain.discord.commands.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.Channel;
import org.jetbrains.annotations.Nullable;

public class EditChannelSubcommand extends Subcommand {
    public EditChannelSubcommand() {
        super(CommandMeta.builder().name("edit").description("Edits an existing channel")
                .addOption("channel", "The channel to edit", OptionMeta.Type.CHANNEL, true)
                .addOption("name", "The new name for the channel", OptionMeta.Type.STRING, false)
                .addOption("topic", "The new topic for the channel", OptionMeta.Type.STRING, false)
                .build());
    }

    @Override
    public void execute(CommandContext context) {
        Channel channel = (Channel) context.getOption("channel");
        @Nullable String newName = (String) context.getOption("name");
        @Nullable String newTopic = (String) context.getOption("topic");

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

        if (newName == null && newTopic == null) {
            context.send("You must specify at least one property to edit (name or topic)!", true);
            return;
        }

        context.defer(true);

        ChannelContext channelContext = guild.getChannel(channel.getIdLong()).orElse(null);
        if (channelContext == null) {
            context.send("Channel not found in this guild!", true);
            return;
        }

        StringBuilder response = new StringBuilder("Channel <#" + channel.getIdLong() + "> updated!");

        if (newName != null) {
            channelContext.setName(newName);
            response.append("\n- Name changed to: `").append(newName).append("`");
        }

        if (newTopic != null) {
            channelContext.setTopic(newTopic);
            response.append("\n- Topic changed to: `").append(newTopic).append("`");
        }

        context.send(response.toString());
    }
}
