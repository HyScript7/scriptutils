package io.github.hyscript7.scriptutils.modules.logging.api;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;

public record LogEntry(JDA jda, long guildId, LogAction action, MessageEmbed embed) {
    public LogCategory category() {
        return action.getCategory();
    }
}
