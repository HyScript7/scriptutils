package io.github.hyscript7.scriptutils.infrastructure.discord;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.infrastructure.Constants;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

@Slf4j
public class SlashGuildContext implements GuildContext {
    private final JDA jda;
    private final Guild guild;

    public SlashGuildContext(JDA jda, long guildId) {
        this.jda = jda;
        this.guild = jda.getGuildById(guildId);
        if (guild == null) {
            throw new IllegalStateException("Guild not found. Is the cache up to date? (Guild ID: " + guildId + ")");
        }
    }

    private Member getMember(long userId) {
        return guild.retrieveMemberById(userId).complete();
    }

    private Optional<User> getUser(long userId) {
        try {
            return Optional.of(jda.retrieveUserById(userId).complete());
        } catch (Exception e) {
            log.error("Could not retrieve user " + userId + " due to an exception.", e);
            return Optional.empty();
        }
    }

    @Override
    public long getGuildId() {
        return guild.getIdLong();
    }

    @Override
    public void timeout(long userId, Duration duration, String reason) {
        Member member = getMember(userId);
        guild.timeoutFor(member, duration).reason(reason).queue();
    }

    @Override
    public void timeout(long userId, Duration duration) {
        timeout(userId, duration, Constants.DEFAULT_REASON);
    }

    @Override
    public void kick(long userId, String reason) {
        Member member = getMember(userId);
        guild.kick(member).reason(reason).queue();
    }

    @Override
    public void kick(long userId) {
        kick(userId, Constants.DEFAULT_REASON);
    }

    @Override
    public void ban(long userId, Duration purgeDuration, String reason) {
        Member member = getMember(userId);
        guild.ban(List.of(member), purgeDuration).reason(reason).queue();
    }

    @Override
    public void ban(long userId, Duration purgeDuration) {
        ban(userId, purgeDuration, Constants.DEFAULT_REASON);
    }

    @Override
    public void ban(long userId, String reason) {
        ban(userId, Constants.DEFAULT_PURGE_DURATION, reason);
    }

    @Override
    public void ban(long userId) {
        ban(userId, Constants.DEFAULT_PURGE_DURATION, Constants.DEFAULT_REASON);
    }

    @Override
    public void unban(long userId) {
        Optional<User> userOptional = getUser(userId);
        if (userOptional.isEmpty()) {
            return;
        }
        guild.unban(userOptional.get()).queue();
    }

    @Override
    public boolean memberHasPermission(long userId, long permissions) {
        Member member = getMember(userId);
        return member.hasPermission(Permission.getPermissions(permissions));
    }

}
