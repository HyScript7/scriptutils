package io.github.hyscript7.scriptutils.modules.multichat.internal.commands;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;
import io.github.hyscript7.scriptutils.modules.multichat.internal.models.MultichatBinding;
import io.github.hyscript7.scriptutils.modules.multichat.internal.services.GroupService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.IncomingWebhookClient;
import net.dv8tion.jda.api.entities.WebhookClient;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Component
public class GroupLeaveCommand extends Subcommand {

    private final GroupService groupService;

    public GroupLeaveCommand(GroupService groupService) {
        super(new CommandMeta("leave", "Leave the multi-chat group the channel is a member of.",
                List.of(new OptionMeta("channel", "The channel to unlink.", OptionMeta.Type.CHANNEL, false))));
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
        long channelId;
        try {
            channelId = ((GuildChannelUnion) context.getOption("channel")).getIdLong();
        } catch (IllegalArgumentException e) {
            channelId = context.getChannelId();
        }
        Optional<MultichatBinding> membership = groupService.getChannelGroup(channelId);
        if (membership.isEmpty()) {
            context.send("This channel is not a member of any group!", true);
            return;
        }
        deleteWebhook((SlashCommandInteractionEvent) context.getEvent(), channelId, membership.get());
        groupService.leaveGroup(membership.get());
        context.send("Channel <#" + channelId + "> has been unlinked from the group `"
                + membership.get().getGroup().getName() + "`.", true);
    }

    // TODO: Move webhook deletion to GuildContext and ChannelContext respectively
    private void deleteWebhook(SlashCommandInteractionEvent event, long channelId, MultichatBinding membership) {
        TextChannel channel = event.getJDA().getChannelById(TextChannel.class, channelId);
        if (channel == null) {
            return;
        }
        // Creating a client might be redundant, but it's a sure way to get the ID even
        // if the URI format changes.
        IncomingWebhookClient webhookClient = WebhookClient.createClient(event.getJDA(), membership.getWebhookUrl());
        channel.deleteWebhookById(webhookClient.getId()).queue();
    }

}
