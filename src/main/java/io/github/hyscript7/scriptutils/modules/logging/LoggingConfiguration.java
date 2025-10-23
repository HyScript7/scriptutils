package io.github.hyscript7.scriptutils.modules.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.hyscript7.scriptutils.modules.logging.api.services.DiscordLoggingService;
import io.github.hyscript7.scriptutils.modules.logging.internal.listeners.MemberLoggingListener;
import io.github.hyscript7.scriptutils.modules.logging.internal.listeners.ModerationLoggingListener;

@Configuration
public class LoggingConfiguration {
    @Bean
    MemberLoggingListener memberLoggingListener(DiscordLoggingService discordLoggingService) {
        return new MemberLoggingListener(discordLoggingService);
    }

    @Bean
    ModerationLoggingListener moderationLoggingListener(DiscordLoggingService discordLoggingService) {
        return new ModerationLoggingListener(discordLoggingService);
    }

    @Bean
    LoggingModule loggingModule(MemberLoggingListener memberLoggingListener,
            ModerationLoggingListener moderationLoggingListener) {
        LoggingModule loggingModule = new LoggingModule();
        loggingModule.addEventListener(memberLoggingListener);
        loggingModule.addEventListener(moderationLoggingListener);
        return loggingModule;
    }

}
