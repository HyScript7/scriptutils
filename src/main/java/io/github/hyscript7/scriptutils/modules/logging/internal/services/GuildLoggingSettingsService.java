package io.github.hyscript7.scriptutils.modules.logging.internal.services;

import io.github.hyscript7.scriptutils.modules.logging.api.LogCategory;
import io.github.hyscript7.scriptutils.modules.logging.internal.models.GuildLoggingSettings;
import io.github.hyscript7.scriptutils.modules.logging.internal.repositories.GuildLoggingSettingsRepository;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class GuildLoggingSettingsService {
    private final GuildLoggingSettingsRepository guildLoggingSettingsRepository;

    public GuildLoggingSettingsService(GuildLoggingSettingsRepository guildLoggingSettingsRepository) {
        this.guildLoggingSettingsRepository = guildLoggingSettingsRepository;
    }

    /**
     * Gets the server settings for a given guild context.
     * If the settings do not exist, creates a default settings for the guild.
     * 
     * @param guildId the ID of the guild
     * @return the server settings for the guild
     */
    public GuildLoggingSettings getSettingsForGuild(long guildId) {
        return guildLoggingSettingsRepository.findByGuildId(guildId).orElseGet(
                () -> createDefaultSettingsForGuild(guildId));
    }

    /**
     * Creates a default server settings for a given guild context.
     * 
     * @param guildId the ID of the guild
     * @return the default server settings for the guild
     */
    public GuildLoggingSettings createDefaultSettingsForGuild(long guildId) {
        GuildLoggingSettings settings = new GuildLoggingSettings();
        settings.setGuildId(guildId);
        return guildLoggingSettingsRepository.save(settings);
    }

    /**
     * Sets the webhook URL for a given guild context and log category.
     * 
     * @param guildId The ID of the guild
     * @param category     The log category
     * @param url          The webhook URL
     */
    public void setWebhookUrl(long guildId, LogCategory category, @Nullable String url) {
        GuildLoggingSettings settings = getSettingsForGuild(guildId);
        switch (category) {
            case LogCategory.MESSAGE -> settings.setMessageWebhookUrl(url);
            case LogCategory.MEMBER -> settings.setMemberWebhookUrl(url);
            case LogCategory.SERVER -> settings.setServerWebhookUrl(url);
            case LogCategory.VOICE -> settings.setVoiceWebhookUrl(url);
            case LogCategory.MODERATION -> settings.setModerationWebhookUrl(url);
            case LogCategory.AUTO_MODERATION -> settings.setAutoModerationWebhookUrl(url);
            case LogCategory.DEFAULT -> settings.setDefaultWebhookUrl(url);
        }
        guildLoggingSettingsRepository.save(settings);
    }

    /**
     * Gets the webhook URL for a given guild context and log category.
     * 
     * @param guildId the ID of the guild
     * @param category     the log category
     * @return the webhook URL for the guild and log category, or an empty Optional
     *         if the webhook URL is not set.
     */
    public Optional<String> getWebhookUrl(long guildId, LogCategory category) {
        GuildLoggingSettings settings = getSettingsForGuild(guildId);
        return Optional.ofNullable(switch (category) {
            case LogCategory.MESSAGE -> settings.getMessageWebhookUrl();
            case LogCategory.MEMBER -> settings.getMemberWebhookUrl();
            case LogCategory.SERVER -> settings.getServerWebhookUrl();
            case LogCategory.VOICE -> settings.getVoiceWebhookUrl();
            case LogCategory.MODERATION -> settings.getModerationWebhookUrl();
            case LogCategory.AUTO_MODERATION -> settings.getAutoModerationWebhookUrl();
            case LogCategory.DEFAULT -> settings.getDefaultWebhookUrl();
        });
    }
}
