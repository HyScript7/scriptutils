package io.github.hyscript7.scriptutils.modules.moderation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.hyscript7.scriptutils.modules.moderation.internal.commands.BanCommand;
import io.github.hyscript7.scriptutils.modules.moderation.internal.commands.KickCommand;
import io.github.hyscript7.scriptutils.modules.moderation.internal.commands.ModerationCommandGroup;
import io.github.hyscript7.scriptutils.modules.moderation.internal.commands.TimeoutCommand;

@Configuration
public class ModerationConfiguration {
    @Bean
    KickCommand kickCommand() {
        return new KickCommand();
    }

    @Bean
    BanCommand banCommand() {
        return new BanCommand();
    }

    @Bean
    TimeoutCommand timeoutCommand() {
        return new TimeoutCommand();
    }

    @Bean
    ModerationCommandGroup moderationCommandGroup(KickCommand kickCommand, BanCommand banCommand, TimeoutCommand timeoutCommand) {
        ModerationCommandGroup commandGroup = new ModerationCommandGroup();
        commandGroup.addSubcommand(kickCommand);
        commandGroup.addSubcommand(banCommand);
        commandGroup.addSubcommand(timeoutCommand);
        return commandGroup;
    }

    @Bean
    ModerationModule moderationModule(ModerationCommandGroup moderationCommandGroup) {
        ModerationModule module = new ModerationModule();
        module.addCommand(moderationCommandGroup);
        return module;
    }
}
