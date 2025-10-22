package io.github.hyscript7.scriptutils.modules.moderation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.hyscript7.scriptutils.modules.moderation.internal.commands.BanCommand;
import io.github.hyscript7.scriptutils.modules.moderation.internal.commands.KickCommand;
import io.github.hyscript7.scriptutils.modules.moderation.internal.commands.ModerationCommandGroup;
import io.github.hyscript7.scriptutils.modules.moderation.internal.commands.TimeoutCommand;
import io.github.hyscript7.scriptutils.modules.moderation.internal.commands.WarnAddCommand;
import io.github.hyscript7.scriptutils.modules.moderation.internal.commands.WarnListCommand;
import io.github.hyscript7.scriptutils.modules.moderation.internal.commands.WarnRevokeCommand;
import io.github.hyscript7.scriptutils.modules.moderation.internal.commands.WarnSubcommandGroup;
import io.github.hyscript7.scriptutils.modules.moderation.internal.services.WarningService;

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
    WarnAddCommand warnAddCommand(WarningService warningService) {
        return new WarnAddCommand(warningService);
    }

    @Bean
    WarnListCommand warnListCommand(WarningService warningService) {
        return new WarnListCommand(warningService);
    }

    @Bean
    WarnRevokeCommand warnRevokeCommand(WarningService warningService) {
        return new WarnRevokeCommand(warningService);
    }

    @Bean
    WarnSubcommandGroup warnSubcommandGroup(WarnAddCommand warnAddCommand, WarnListCommand warnListCommand,
            WarnRevokeCommand warnRevokeCommand) {
        WarnSubcommandGroup commandGroup = new WarnSubcommandGroup();
        commandGroup.addSubcommand(warnAddCommand);
        commandGroup.addSubcommand(warnListCommand);
        commandGroup.addSubcommand(warnRevokeCommand);
        return commandGroup;
    }

    @Bean
    ModerationCommandGroup moderationCommandGroup(KickCommand kickCommand, BanCommand banCommand,
            TimeoutCommand timeoutCommand, WarnSubcommandGroup warnSubcommandGroup) {
        ModerationCommandGroup commandGroup = new ModerationCommandGroup();
        commandGroup.addSubcommand(kickCommand);
        commandGroup.addSubcommand(banCommand);
        commandGroup.addSubcommand(timeoutCommand);
        commandGroup.addSubcommandGroup(warnSubcommandGroup);
        return commandGroup;
    }

    @Bean
    ModerationModule moderationModule(ModerationCommandGroup moderationCommandGroup) {
        ModerationModule module = new ModerationModule();
        module.addCommand(moderationCommandGroup);
        return module;
    }
}
