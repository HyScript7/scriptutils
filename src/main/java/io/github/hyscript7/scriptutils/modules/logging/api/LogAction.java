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
    MEMBER_NOTE_UPDATED(LogCategory.MODERATION, "Member Note Updated", "Fired when a note is updated or created."),;

    private final LogCategory category;
    private final String name;
    private final String description;

    private LogAction(LogCategory category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }
}
