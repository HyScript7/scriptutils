package io.github.hyscript7.scriptutils.modules.logging.api.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.hyscript7.scriptutils.modules.logging.api.LogEntry;
import io.github.hyscript7.scriptutils.modules.logging.api.LogCategory;
import io.github.hyscript7.scriptutils.modules.logging.internal.models.GuildLoggingSettings;
import io.github.hyscript7.scriptutils.modules.logging.internal.services.GuildLoggingSettingsService;
import net.dv8tion.jda.api.entities.IncomingWebhookClient;
import net.dv8tion.jda.api.entities.WebhookClient;

@Service
public class DiscordLoggingService {

    private final GuildLoggingSettingsService guildLoggingSettingsService;

    DiscordLoggingService(GuildLoggingSettingsService guildLoggingSettingsService) {
        this.guildLoggingSettingsService = guildLoggingSettingsService;
    }

    public void log(LogEntry action) {
        GuildLoggingSettings settings = guildLoggingSettingsService.getSettingsForGuild(action.guildId());
        Optional<String> defaultWebhookUrl = Optional.ofNullable(settings.getDefaultWebhookUrl());
        Optional<String> webhookUrl = Optional.ofNullable(switch (action.category()) {
            case LogCategory.DEFAULT -> settings.getDefaultWebhookUrl();
            case LogCategory.MESSAGE -> settings.getMessageWebhookUrl();
            case LogCategory.MEMBER -> settings.getMemberWebhookUrl();
            case LogCategory.SERVER -> settings.getServerWebhookUrl();
            case LogCategory.VOICE -> settings.getVoiceWebhookUrl();
            case LogCategory.MODERATION -> settings.getModerationWebhookUrl();
            case LogCategory.AUTO_MODERATION -> settings.getAutoModerationWebhookUrl();
        });

        if (webhookUrl.isEmpty() && defaultWebhookUrl.isEmpty()) {
            return;
        }

        if (webhookUrl.isEmpty()) {
            webhookUrl = defaultWebhookUrl;
        }

        IncomingWebhookClient webhookClient = WebhookClient.createClient(action.jda(), webhookUrl.get());
        webhookClient.sendMessageEmbeds(action.embed()).queue();
    }

}
