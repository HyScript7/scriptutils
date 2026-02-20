package io.github.hyscript7.scriptutils.modules.logging.internal.commands;

import io.github.hyscript7.scriptutils.domain.discord.commands.*;
import io.github.hyscript7.scriptutils.modules.logging.api.LogCategory;
import io.github.hyscript7.scriptutils.modules.logging.internal.services.GuildLoggingSettingsService;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LoggingEnableSubcommand extends AbstractLoggingSubcommand {

    public LoggingEnableSubcommand(GuildLoggingSettingsService guildLoggingSettingsService) {
        super(new CommandMeta("enable", "Enables a specific logging category", List.of(
                new OptionMeta("category", "The logging category to enable", OptionMeta.Type.STRING, true),
                new OptionMeta("channel", "The channel to log to", OptionMeta.Type.CHANNEL, true)
        )), guildLoggingSettingsService);
    }

    @Override
    public void execute(CommandContext context) {
        GuildContext guild = getValidatedGuild(context);
        if (guild == null) return;

        LogCategory logCategory = getValidatedCategory(context);
        if (logCategory == null) return;

        Channel channel = (Channel) context.getOption("channel");
        if (!(channel instanceof TextChannel)) {
            context.send("Please specify a valid **text channel**!", true);
            return;
        }

        Optional<ChannelContext> channelContext = guild.getChannel(channel.getIdLong());
        if (channelContext.isEmpty()) {
            context.send("The provided channel is inaccessible or has been deleted.", true);
            return;
        }

        String webhookUrl = channelContext.get().createWebhook(logCategory.name() + " LOGGER");

        guildLoggingSettingsService.setWebhookUrl(guild.getGuildId(), logCategory, webhookUrl);

        context.send("**" + logCategory.name() + "** events will now be sent into " + channel.getAsMention() + "!\nURL: ||`" + webhookUrl + "`||", true);
    }
}