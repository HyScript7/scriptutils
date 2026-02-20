package io.github.hyscript7.scriptutils.modules.logging.internal.commands;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.modules.logging.api.LogCategory;
import io.github.hyscript7.scriptutils.modules.logging.internal.services.GuildLoggingSettingsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoggingDisableSubcommand extends AbstractLoggingSubcommand {

    public LoggingDisableSubcommand(GuildLoggingSettingsService guildLoggingSettingsService) {
        super(new CommandMeta("disable", "Disables a specific logging category", List.of(
                new OptionMeta("category", "The logging category to disable", OptionMeta.Type.STRING, true)
        )), guildLoggingSettingsService);
    }

    @Override
    public void execute(CommandContext context) {
        GuildContext guild = getValidatedGuild(context);
        if (guild == null) return;

        LogCategory logCategory = getValidatedCategory(context);
        if (logCategory == null) return;

        // If category is null, it means it is disabled.
        guildLoggingSettingsService.setWebhookUrl(guild.getGuildId(), logCategory, null);

        context.send("**" + logCategory.name() + "** logging has been disabled.", true);
    }
}
