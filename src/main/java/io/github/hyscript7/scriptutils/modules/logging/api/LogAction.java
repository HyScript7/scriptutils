package io.github.hyscript7.scriptutils.modules.logging.api;

import java.util.Map;

public record LogAction(long guildId, String name, LogCategory category, Map<String, String> fields) {
}
