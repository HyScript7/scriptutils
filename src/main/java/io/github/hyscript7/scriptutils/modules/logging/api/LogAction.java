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
    MEMBER_UNTIMEOUT(LogCategory.MEMBER, "Member Untimeout", "Fired when a member is untimeouted.");

    private final LogCategory category;
    private final String name;
    private final String description;

    private LogAction(LogCategory category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }
}
