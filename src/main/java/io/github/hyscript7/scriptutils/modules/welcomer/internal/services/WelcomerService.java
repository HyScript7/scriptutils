package io.github.hyscript7.scriptutils.modules.welcomer.internal.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.hyscript7.scriptutils.modules.welcomer.internal.models.GuildWelcomerSettings;
import io.github.hyscript7.scriptutils.modules.welcomer.internal.repositories.GuildWelcomerSettingsRepository;

@Service
public class WelcomerService {
    
    private final GuildWelcomerSettingsRepository settingsRepository;
    
    public WelcomerService(GuildWelcomerSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }
    
    /**
     * Gets the welcomer settings for a guild, creating default settings if none exist
     * 
     * @param guildId The ID of the guild
     * @return The welcomer settings for the guild
     */
    public GuildWelcomerSettings getSettings(long guildId) {
        return settingsRepository.findByGuildId(guildId)
                .orElseGet(() -> createDefaultSettings(guildId));
    }
    
    /**
     * Creates default welcomer settings for a guild
     * 
     * @param guildId The ID of the guild
     * @return The newly created default settings
     */
    private GuildWelcomerSettings createDefaultSettings(long guildId) {
        GuildWelcomerSettings settings = GuildWelcomerSettings.builder()
                .guildId(guildId)
                .enabled(false)
                .welcomeMessage("Welcome to {guild}, {mention}!")
                .channelId(null)
                .sendViaDm(false)
                .build();
        return settingsRepository.save(settings);
    }
    
    /**
     * Updates the welcomer settings for a guild
     * 
     * @param settings The settings to save
     * @return The saved settings
     */
    public GuildWelcomerSettings updateSettings(GuildWelcomerSettings settings) {
        return settingsRepository.save(settings);
    }
    
    /**
     * Enables or disables the welcomer for a guild
     * 
     * @param guildId The ID of the guild
     * @param enabled Whether to enable or disable the welcomer
     */
    public void setEnabled(long guildId, boolean enabled) {
        GuildWelcomerSettings settings = getSettings(guildId);
        settings.setEnabled(enabled);
        updateSettings(settings);
    }
    
    /**
     * Sets the welcome message for a guild
     * 
     * @param guildId The ID of the guild
     * @param message The welcome message template
     */
    public void setWelcomeMessage(long guildId, String message) {
        GuildWelcomerSettings settings = getSettings(guildId);
        settings.setWelcomeMessage(message);
        updateSettings(settings);
    }
    
    /**
     * Sets the channel to send welcome messages to
     * 
     * @param guildId The ID of the guild
     * @param channelId The ID of the channel (null for DM)
     */
    public void setChannel(long guildId, Long channelId) {
        GuildWelcomerSettings settings = getSettings(guildId);
        settings.setChannelId(channelId);
        settings.setSendViaDm(channelId == null);
        updateSettings(settings);
    }
    
    /**
     * Sets whether to send welcome messages via DM
     * 
     * @param guildId The ID of the guild
     * @param sendViaDm Whether to send via DM
     */
    public void setSendViaDm(long guildId, boolean sendViaDm) {
        GuildWelcomerSettings settings = getSettings(guildId);
        settings.setSendViaDm(sendViaDm);
        if (sendViaDm) {
            settings.setChannelId(null);
        }
        updateSettings(settings);
    }
    
    /**
     * Formats a welcome message with the given placeholders
     * 
     * @param template The message template
     * @param username The username of the member
     * @param mention The mention string for the member
     * @param guildName The name of the guild
     * @return The formatted message
     */
    public String formatWelcomeMessage(String template, String username, String mention, String guildName) {
        return template
                .replace("{username}", username)
                .replace("{mention}", mention)
                .replace("{guild}", guildName);
    }
}
