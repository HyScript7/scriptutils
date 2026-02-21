package io.github.hyscript7.scriptutils.modules.welcomer.internal.commands;

import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;
import io.github.hyscript7.scriptutils.modules.welcomer.internal.models.GuildWelcomerSettings;
import io.github.hyscript7.scriptutils.modules.welcomer.internal.services.WelcomerService;
import net.dv8tion.jda.api.Permission;

@Component
public class StatusCommand extends Subcommand {
    
    private final WelcomerService welcomerService;
    
    public StatusCommand(WelcomerService welcomerService) {
        super(new CommandMeta("status", "Shows the current welcomer configuration."));
        this.welcomerService = welcomerService;
    }
    
    @Override
    public void execute(CommandContext context) {
        Optional<GuildContext> guildOptional = context.getGuild();
        if (guildOptional.isEmpty()) {
            context.send("You must be in a guild to use this command!", true);
            return;
        }
        GuildContext guild = guildOptional.get();
        
        if (!guild.memberHasPermission(context.getAuthorId(), Permission.MANAGE_SERVER.getRawValue())) {
            context.send("You must have the `MANAGE SERVER` permission to use this command!", true);
            return;
        }
        
        GuildWelcomerSettings settings = welcomerService.getSettings(guild.getGuildId());
        
        StringBuilder response = new StringBuilder("## Welcomer Configuration\n");
        response.append("**Status:** ").append(settings.getEnabled() ? "✅ Enabled" : "❌ Disabled").append("\n");
        response.append("**Delivery:** ");
        
        if (settings.getSendViaDm()) {
            response.append("Direct Message to new member\n");
        } else if (settings.getChannelId() != null) {
            response.append("<#").append(settings.getChannelId()).append(">\n");
        } else {
            response.append("Not configured\n");
        }
        
        response.append("**Message:**\n```\n").append(settings.getWelcomeMessage()).append("\n```\n");
        response.append("\n*Available placeholders: `{username}`, `{mention}`, `{guild}`*");
        
        context.send(response.toString(), true);
    }
}
