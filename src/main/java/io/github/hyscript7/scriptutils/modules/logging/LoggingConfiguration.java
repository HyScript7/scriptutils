package io.github.hyscript7.scriptutils.modules.logging;

import io.github.hyscript7.scriptutils.modules.logging.internal.commands.LoggingCommandGroup;
import io.github.hyscript7.scriptutils.modules.logging.internal.commands.LoggingDisableSubcommand;
import io.github.hyscript7.scriptutils.modules.logging.internal.commands.LoggingEnableSubcommand;
import io.github.hyscript7.scriptutils.modules.logging.internal.commands.LoggingStatusSubcommand;
import io.github.hyscript7.scriptutils.modules.logging.internal.services.GuildLoggingSettingsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.hyscript7.scriptutils.modules.logging.api.services.DiscordLoggingService;
import io.github.hyscript7.scriptutils.modules.logging.internal.listeners.MemberLoggingListener;
import io.github.hyscript7.scriptutils.modules.logging.internal.listeners.ModerationLoggingListener;
import io.github.hyscript7.scriptutils.modules.logging.internal.listeners.ServerLoggingListener;
import io.github.hyscript7.scriptutils.modules.logging.internal.listeners.VoiceLoggingListener;

@Configuration
public class LoggingConfiguration {
    @Bean
    LoggingStatusSubcommand loggingStatusSubcommand(GuildLoggingSettingsService guildLoggingSettingsService) {
        return new LoggingStatusSubcommand(guildLoggingSettingsService);
    }

    @Bean
    LoggingEnableSubcommand loggingEnableSubcommand(GuildLoggingSettingsService guildLoggingSettingsService) {
        return new LoggingEnableSubcommand(guildLoggingSettingsService);
    }

    @Bean
    LoggingDisableSubcommand loggingDisableSubcommand(GuildLoggingSettingsService guildLoggingSettingsService) {
        return new LoggingDisableSubcommand(guildLoggingSettingsService);
    }

    @Bean
    LoggingCommandGroup loggingCommandGroup(LoggingStatusSubcommand loggingStatusSubcommand, LoggingEnableSubcommand loggingEnableSubcommand, LoggingDisableSubcommand loggingDisableSubcommand) {
        LoggingCommandGroup loggingCommandGroup = new LoggingCommandGroup();
        loggingCommandGroup.addSubcommand(loggingStatusSubcommand);
        loggingCommandGroup.addSubcommand(loggingDisableSubcommand);
        loggingCommandGroup.addSubcommand(loggingEnableSubcommand);
        return loggingCommandGroup;
    }

    @Bean
    MemberLoggingListener memberLoggingListener(DiscordLoggingService discordLoggingService) {
        return new MemberLoggingListener(discordLoggingService);
    }

    @Bean
    ModerationLoggingListener moderationLoggingListener(DiscordLoggingService discordLoggingService) {
        return new ModerationLoggingListener(discordLoggingService);
    }

    @Bean
    ServerLoggingListener serverLoggingListener(DiscordLoggingService discordLoggingService) {
        return new ServerLoggingListener(discordLoggingService);
    }

    @Bean
    VoiceLoggingListener voiceLoggingListener(DiscordLoggingService discordLoggingService) {
        return new VoiceLoggingListener(discordLoggingService);
    }

    @Bean
    LoggingModule loggingModule(
            MemberLoggingListener memberLoggingListener,
            ModerationLoggingListener moderationLoggingListener,
            ServerLoggingListener serverLoggingListener,
            VoiceLoggingListener voiceLoggingListener,
            LoggingCommandGroup loggingCommandGroup
    ) {
        LoggingModule loggingModule = new LoggingModule();
        loggingModule.addEventListener(memberLoggingListener);
        loggingModule.addEventListener(moderationLoggingListener);
        loggingModule.addEventListener(serverLoggingListener);
        loggingModule.addEventListener(voiceLoggingListener);
        loggingModule.addCommand(loggingCommandGroup);
        return loggingModule;
    }

}
