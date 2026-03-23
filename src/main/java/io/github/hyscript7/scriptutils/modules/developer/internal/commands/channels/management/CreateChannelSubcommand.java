package io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels.management;

import io.github.hyscript7.scriptutils.domain.discord.commands.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import org.jetbrains.annotations.Nullable;

public class CreateChannelSubcommand extends Subcommand {
    public CreateChannelSubcommand() {
        super(CommandMeta.builder().name("create").description("Creates a new channel")
                .addOption("type", "The type of channel to create", OptionMeta.Type.STRING, true)
                .addOption("name", "The name of the channel", OptionMeta.Type.STRING, true)
                .addOption("category", "The category to create the channel in", OptionMeta.Type.CHANNEL, false, null)
                .build());
    }

    @Override
    public void execute(CommandContext context) {
        String typeString = (String) context.getOption("type");
        String name = (String) context.getOption("name");
        @Nullable Category category = (Category) context.getOption("category");

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

        ChannelType channelType = parseChannelType(typeString);
        if (channelType == null) {
            context.send("Invalid channel type! Valid types: `text`, `voice`, `category`, `stage`, `forum`", true);
            return;
        }

        context.defer(true);

        ChannelContext newChannel = guild.createChannel(
                channelType,
                name,
                category != null ? category.getIdLong() : null
        );

        context.send("Channel <#" + newChannel.getChannelId() + "> created!");
    }

    private ChannelType parseChannelType(String typeString) {
        // Mind that this gets rid of spaces as well
        String cleaned = typeString.toLowerCase().replaceAll("[^a-z]", "");

        return switch (cleaned) {
            case "text", "textchannel" -> ChannelType.TEXT;
            case "voice", "voicechannel" -> ChannelType.VOICE;
            case "category" -> ChannelType.CATEGORY;
            // TODO: Support for announcement channels
            // case "announcement", "news", "announcementchannel" -> ChannelType.ANNOUNCEMENT;
            case "stage", "stagechannel" -> ChannelType.STAGE;
            case "forum", "forumchannel" -> ChannelType.FORUM;
            default -> null;
        };
    }
}
