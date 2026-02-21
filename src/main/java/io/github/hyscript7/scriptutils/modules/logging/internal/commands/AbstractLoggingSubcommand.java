package io.github.hyscript7.scriptutils.modules.logging.internal.commands;

import io.github.hyscript7.scriptutils.domain.discord.commands.*;
import io.github.hyscript7.scriptutils.modules.logging.api.LogCategory;
import io.github.hyscript7.scriptutils.modules.logging.internal.services.GuildLoggingSettingsService;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class AbstractLoggingSubcommand extends Subcommand {
    protected final GuildLoggingSettingsService guildLoggingSettingsService;
    private static final String VALID_LOG_CATEGORY_NAMES = Arrays.stream(LogCategory.values())
            .map(LogCategory::toString)
            .map(s -> "`" + s + "`")
            .collect(Collectors.joining(", "));

    protected AbstractLoggingSubcommand(CommandMeta meta, GuildLoggingSettingsService guildLoggingSettingsService) {
        super(meta);
        this.guildLoggingSettingsService = guildLoggingSettingsService;
    }

    protected LogCategory getValidatedCategory(CommandContext context) {
        String category = (String) context.getOption("category");
        if (category == null) return null;

        // Some people abbreviate moderation to mod, i.e. in "auto moderation", we often refer to it as "auto mod"
        // This performs expansion (alongside replacing spaces with underscores) so that the enum picks it up.
        category = category.toUpperCase().replace(' ', '_').replaceAll("(\\b)MOD(\\b)", "$1MODERATION$2");

        try {
            return LogCategory.valueOf(category);
        } catch (IllegalArgumentException e) {
            context.send("# Invalid category!\nMust be one of: " + VALID_LOG_CATEGORY_NAMES +
                    "\n\nYou can substitute `moderation` for `mod` and use any case of letters."
                    + "\nUnderscores can be replaced with spaces."
                    + "\nFor example: `AuTo MoD` is the same as `AUTO_MODERATION`.", true);
            return null;
        }
    }

    protected GuildContext getValidatedGuild(CommandContext context) {
        return context.getGuild().orElseGet(() -> {
            context.send("You must be in a guild to use this command!", true);
            return null;
        });
    }
}