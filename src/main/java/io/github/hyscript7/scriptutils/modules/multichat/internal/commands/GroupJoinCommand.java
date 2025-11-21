package io.github.hyscript7.scriptutils.modules.multichat.internal.commands;

import java.util.List;
import java.util.Optional;

import io.github.hyscript7.scriptutils.modules.multichat.internal.models.MultichatGroup;
import io.github.hyscript7.scriptutils.modules.multichat.internal.services.GroupService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;

@Component
public class GroupJoinCommand extends Subcommand {

    private final GroupService groupService;

    public GroupJoinCommand(GroupService groupService) {
        super(new CommandMeta("join", "Joins a group.", List.of(
                new OptionMeta("name", "The name of the multi-chat group to join.", OptionMeta.Type.STRING, true),
                new OptionMeta("channel", "The channel to join the group in.", OptionMeta.Type.CHANNEL, false))));
        this.groupService = groupService;
    }

    @Override
    public void execute(CommandContext context) {
        Optional<GuildContext> guildContext = context.getGuild();
        if (guildContext.isEmpty()) {
            context.send("This command can only be used in a guild!", true);
            return;
        }
        if (!guildContext.get().memberHasPermission(context.getAuthorId(), Permission.MANAGE_WEBHOOKS.getRawValue())) {
            context.send("You must have the `MANAGE WEBHOOKS` permission to use this command!", true);
            return;
        }
        String name = (String) context.getOption("name");
        long channelId;
        try {
            channelId = ((GuildChannelUnion) context.getOption("channel")).getIdLong();
        } catch (IllegalArgumentException e) {
            channelId = context.getChannelId();
        }
        Optional<MultichatGroup> groupOptional = groupService.getGroupByNameAndOwnerId(name, context.getAuthorId());
        if (groupOptional.isEmpty()) {
            context.send("Such a group doesn't exist! Did you type the name right? Try `/multichat list`.", true);
            return;
        }
        String webhookUrl = createWebhook((SlashCommandInteractionEvent) context.getEvent(), channelId,
                groupOptional.get());
        if (webhookUrl == null) {
            context.send("Failed to create webhook. Is the channel a text channel?", true);
            return;
        }
        groupService.joinGroup(groupOptional.get(), channelId, guildContext.get().getGuildId(),
                webhookUrl);
        context.send("Channel <#" + channelId + "> has joined the group `" + name + "`. Use `/multichat leave` to leave.", true);
    }

    // TODO: Move low level implementation to GuildContext and ChannelContext
    // respectively
    private String createWebhook(SlashCommandInteractionEvent event, long channelId, MultichatGroup group) {
        TextChannel channel = event.getJDA().getChannelById(TextChannel.class, channelId);
        if (channel == null) {
            return null;
        }
        Webhook webhook = channel.createWebhook(group.getName() + " - SCRIPTUTILS MULTICHAT").complete();
        return webhook.getUrl();
    }
}
