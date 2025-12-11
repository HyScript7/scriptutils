package io.github.hyscript7.scriptutils.infrastructure.discord;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import io.github.hyscript7.scriptutils.domain.discord.commands.ChannelContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.ChannelType;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.RoleContext;
import io.github.hyscript7.scriptutils.infrastructure.Constants;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import org.jetbrains.annotations.Nullable;

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
            log.error("Could not retrieve user {} due to an exception.", userId, e);
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

    @Override
    public Optional<ChannelContext> getChannel(long channelId) {
        Optional<GuildChannel> channel = Optional
                .ofNullable(guild.getChannelById(GuildChannel.class, channelId));
        return channel.map(guildChannel -> new SlashChannelContext(guildChannel, Optional.of(this)));
    }

    @Override
    public ChannelContext createChannel(ChannelType channelType, String name, @Nullable Long categoryId) {
        Category category;
        if (categoryId == null) {
            category = null;
        } else {
            category = guild.getCategoryById(categoryId);
        }
        GuildChannel channel = null;
        switch (channelType) {
            case TEXT -> {
                channel = guild.createTextChannel(name, category).complete();
            }
            case FORUM ->  {
                channel = guild.createForumChannel(name, category).complete();
            }
            case STAGE -> {
                channel = guild.createStageChannel(name, category).complete();
            }
            case VOICE -> {
                channel = guild.createVoiceChannel(name, category).complete();
            }
            case CATEGORY ->  {
                channel = guild.createCategory(name).complete();
            }
        };
        if (channel == null) {
            throw new IllegalStateException("Could not create channel for channelType: " + channelType);
        }
        return new SlashChannelContext(channel, Optional.of(this));
    }

    @Override
    public RoleContext createRole(String name, int color, boolean mentionable, boolean distinct, @Nullable Long positionedAfter) {
        Role role = guild.createRole().setName(name).setColor(color).setMentionable(mentionable).setHoisted(distinct).complete();
        if (positionedAfter != null) {
            Role other = guild.getRoleById(positionedAfter);
            if (other != null) {
                guild.modifyRolePositions().selectPosition(role).moveAbove(other).complete();
            } else {
                // TODO: We're failing silently, let the user know somehow!
                log.warn("Could not modify positions for role {}, as the other role {} does not exist.", role.getId(), positionedAfter);
            }
        }
        return new SlashRoleContext(role, this);
    }

}
