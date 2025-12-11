package io.github.hyscript7.scriptutils.modules.developer.internal;

public enum ChannelAccessType {
    ALLOW, // Normal access, can send and view messages
    READ, // Can only read
    DENY; // Cannot access channel at all
}
