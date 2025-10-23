package io.github.hyscript7.scriptutils.modules.logging.api;

import lombok.Getter;

@Getter
public enum LogAction {
    DEFAULT(LogCategory.DEFAULT, "Default", "The fallback log action."),
    MEMBER_JOINED(LogCategory.MEMBER, "Member Joined", "Fired when a member joins the server."),
    MEMBER_LEFT(LogCategory.MEMBER, "Member Left", "Fired when a member leaves the server."),
    MEMBER_ROLE_ADDED(LogCategory.MEMBER, "Member Role Added", "Fired when a member gains a role."),
    MEMBER_ROLE_REMOVED(LogCategory.MEMBER, "Member Role Removed", "Fired when a member loses a role."),
    MEMBER_NICKNAME_CHANGED(LogCategory.MEMBER, "Member Nickname Changed",
            "Fired when a member changes their nickname."),
    MEMBER_AVATAR_CHANGED(LogCategory.MEMBER, "Member Avatar Changed", "Fired when a member changes their avatar."),

    MEMBER_TIMEOUT(LogCategory.MEMBER, "Member Timeout", "Fired when a member is timed out."),
    MEMBER_UNTIMEOUT(LogCategory.MEMBER, "Member Untimeout", "Fired when a member is untimeouted."),
    MEMBER_BANNED(LogCategory.MODERATION, "Member Banned", "Fired when a member is banned."),
    MEMBER_UNBANNED(LogCategory.MODERATION, "Member Unbanned", "Fired when a member is unbanned."),
    MEMBER_KICKED(LogCategory.MODERATION, "Member Kicked", "Fired when a member is kicked."),
    MEMBER_WARNED(LogCategory.MODERATION, "Member Warned", "Fired when a member is warned."),
    MEMBER_NOTE_UPDATED(LogCategory.MODERATION, "Member Note Updated", "Fired when a note is updated or created."),
    
    GUILD_CHANNEL_CREATED(LogCategory.SERVER, "Guild Channel Created", "Fired when a channel is created."),
    GUILD_CHANNEL_DELETED(LogCategory.SERVER, "Guild Channel Deleted", "Fired when a channel is deleted."),
    GUILD_CHANNEL_UPDATED(LogCategory.SERVER, "Guild Channel Updated", "Fired when a channel is updated (name, position, permissions)."),
    GUILD_ROLE_CREATED(LogCategory.SERVER, "Guild Role Created", "Fired when a role is created."),
    GUILD_ROLE_DELETED(LogCategory.SERVER, "Guild Role Deleted", "Fired when a role is deleted."),
    GUILD_ROLE_UPDATED(LogCategory.SERVER, "Guild Role Updated", "Fired when a role is updated (name, position, permissions)."),
    GUILD_OWNERSHIP_TRANSFER(LogCategory.SERVER, "Guild Ownership Transfer", "Fired when the guild owner is transferred."),

    VOICE_CHANNEL_JOINED(LogCategory.VOICE, "Voice Channel Joined", "Fired when a member joins a voice channel."),
    VOICE_CHANNEL_LEFT(LogCategory.VOICE, "Voice Channel Left", "Fired when a member leaves a voice channel."),
    VOICE_CHANNEL_SWITCHED(LogCategory.VOICE, "Voice Channel Switched", "Fired when a member switches voice channels."),
    ;

    private final LogCategory category;
    private final String name;
    private final String description;

    private LogAction(LogCategory category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }
}
