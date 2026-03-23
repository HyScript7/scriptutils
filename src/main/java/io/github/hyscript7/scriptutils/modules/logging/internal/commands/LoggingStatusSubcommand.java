package io.github.hyscript7.scriptutils.modules.logging.internal.commands;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.modules.logging.internal.models.GuildLoggingSettings;
import io.github.hyscript7.scriptutils.modules.logging.internal.services.GuildLoggingSettingsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoggingStatusSubcommand extends AbstractLoggingSubcommand {
    public LoggingStatusSubcommand(GuildLoggingSettingsService guildLoggingSettingsService) {
        super(new CommandMeta("status", "Shows the current logging configuration for this server", List.of()),
                guildLoggingSettingsService);
    }

    @Override
    public void execute(CommandContext context) {
        GuildContext guild = getValidatedGuild(context);
        if (guild == null) return;

        if (!userHasPermissionsToManageLogging(context)) {
            context.send("You do not have permission to use this command!", true);
            return;
        }

        GuildLoggingSettings settings = guildLoggingSettingsService.getSettingsForGuild(guild.getGuildId());

        String sb = "# 📜 Logging Configuration\n" +
                "**Default**: " + getStatusEmoji(settings.getDefaultWebhookUrl()) + " \n" +
                "**Message**: " + getStatusEmojiForUnsupported(settings.getMessageWebhookUrl()) + " \n" +
                "**Member**: " + getStatusEmoji(settings.getMemberWebhookUrl()) + " \n" +
                "**Server**: " + getStatusEmoji(settings.getServerWebhookUrl()) + " \n" +
                "**Voice**: " + getStatusEmoji(settings.getVoiceWebhookUrl()) + " \n" +
                "**Moderation**: " + getStatusEmoji(settings.getModerationWebhookUrl()) + " \n" +
                "**Auto Moderation**: " + getStatusEmoji(settings.getAutoModerationWebhookUrl()) + " \n" +
                "\n*Use `/logging enable <category> <channel>` to activate a category.*" +
                "\nIf default is set, all unset categories will go to that channel.";

        context.send(sb, true);
    }

    private String getStatusEmoji(String webhookUrl) {
        return (webhookUrl != null && !webhookUrl.isBlank()) ? "✅ `Enabled`" : "❌ `Disabled`";
    }

    private String getStatusEmojiForUnsupported(String webhookUrl) {
        return (webhookUrl != null && !webhookUrl.isBlank()) ? "⚠️ `Unsupported`" : "❌ `Disabled`";
    }
}