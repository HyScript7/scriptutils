package io.github.hyscript7.scriptutils.modules.multichat.internal.listeners;

import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.modules.multichat.internal.models.MultichatBinding;
import io.github.hyscript7.scriptutils.modules.multichat.internal.services.GroupService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.IncomingWebhookClient;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.WebhookClient;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
@Slf4j
public class OnMessageListener extends ListenerAdapter {

    private record MessageDTO(String username, long userId, String avatarUrl, TemporalAccessor sendAtTimestamp,
            String sourceGuildName, String sourceGuildIconUrl, long sourceGuildId, String content) {
    }

    private final GroupService groupService;

    public OnMessageListener(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    @Transactional
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        if (user.isBot() || user.isSystem()) {
            return;
        }
        long sourceChannelId = event.getMessage().getChannelIdLong();
        Optional<MultichatBinding> membership = groupService.getChannelGroup(sourceChannelId);
        if (membership.isEmpty())
            return;
        MessageDTO message = new MessageDTO(event.getAuthor().getName(), event.getAuthor().getIdLong(),
                event.getAuthor().getAvatarUrl(), event.getMessage().getTimeCreated(), event.getGuild().getName(),
                event.getGuild().getIconUrl(), event.getGuild().getIdLong(), event.getMessage().getContentDisplay());
        JDA jda = event.getJDA();
        groupService.getNeighbors(membership.get()).stream().filter(ms -> ms.getChannelId() != sourceChannelId)
                .map(MultichatBinding::getWebhookUrl).forEach(
                        url -> trySendToWebhook(jda, url, message));
        event.getMessage().addReaction(Emoji.fromUnicode("☑️")).queue();
    }

    /**
     * Attempts to send a message to a webhook with the given JDA and message
     * ! If the attempt fails, logs a warning and un-registers the multi-chat group
     * member
     * 
     * @param jda     the JDA containing the webhook URL
     * @param message the message to be sent to the webhook
     */
    private void trySendToWebhook(JDA jda, String webhookUrl, MessageDTO message) {
        IncomingWebhookClient webhookClient;
        try {
            webhookClient = WebhookClient.createClient(jda, webhookUrl);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to create webhook client {}, unregistering multi-chat link!", webhookUrl, webhookUrl, e);
            groupService.unregisterViaWebhook(webhookUrl);
            return;
        }
        // We don't try/catch elsewhere, because if one attempt fails, the others do
        // too.
        // Let Spring and JDA handle the error.
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTimestamp(message.sendAtTimestamp());
        if (message.avatarUrl != null) {
            embedBuilder.setThumbnail(message.avatarUrl);
            embedBuilder.setAuthor(message.username, "https://discord.com/users/" + message.userId, message.avatarUrl);
        } else {
            embedBuilder.setAuthor(message.username, "https://discord.com/users/" + message.userId);
        }
        embedBuilder.setDescription(message.content);
        embedBuilder.setFooter(message.sourceGuildName + " (" + message.sourceGuildId + ")",
                message.sourceGuildIconUrl);
        webhookClient.sendMessageEmbeds(embedBuilder.build()).queue();
    }

}
