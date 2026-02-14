package io.github.hyscript7.scriptutils.modules.welcomer.internal.listeners;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.modules.welcomer.internal.models.GuildWelcomerSettings;
import io.github.hyscript7.scriptutils.modules.welcomer.internal.services.WelcomerService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
@Slf4j
public class MemberJoinListener extends ListenerAdapter {
    
    private final WelcomerService welcomerService;
    
    public MemberJoinListener(WelcomerService welcomerService) {
        this.welcomerService = welcomerService;
    }
    
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member member = event.getMember();
        User user = member.getUser();
        Guild guild = event.getGuild();
        
        // Ignore bots
        if (user.isBot()) {
            return;
        }
        
        GuildWelcomerSettings settings = welcomerService.getSettings(guild.getIdLong());
        
        // Check if welcomer is enabled
        if (!Boolean.TRUE.equals(settings.getEnabled())) {
            return;
        }
        
        // Format the welcome message
        String formattedMessage = welcomerService.formatWelcomeMessage(
                settings.getWelcomeMessage(),
                user.getName(),
                user.getAsMention(),
                guild.getName()
        );
        
        // Send the message
        if (Boolean.TRUE.equals(settings.getSendViaDm())) {
            // Send via DM
            user.openPrivateChannel().queue(
                    privateChannel -> privateChannel.sendMessage(formattedMessage).queue(
                            success -> log.debug("Sent welcome DM to user {} in guild {}", user.getId(), guild.getId()),
                            error -> log.warn("Failed to send welcome DM to user {} in guild {}: {}", 
                                    user.getId(), guild.getId(), error.getMessage())
                    ),
                    error -> log.warn("Failed to open DM channel for user {} in guild {}: {}", 
                            user.getId(), guild.getId(), error.getMessage())
            );
        } else if (settings.getChannelId() != null) {
            // Send to channel
            TextChannel channel = guild.getTextChannelById(settings.getChannelId());
            if (channel != null) {
                channel.sendMessage(formattedMessage).queue(
                        success -> log.debug("Sent welcome message to channel {} in guild {}", 
                                settings.getChannelId(), guild.getId()),
                        error -> log.warn("Failed to send welcome message to channel {} in guild {}: {}", 
                                settings.getChannelId(), guild.getId(), error.getMessage())
                );
            } else {
                log.warn("Welcome channel {} not found in guild {}", settings.getChannelId(), guild.getId());
            }
        } else {
            log.warn("Welcomer enabled for guild {} but no channel or DM configured", guild.getId());
        }
    }
}
