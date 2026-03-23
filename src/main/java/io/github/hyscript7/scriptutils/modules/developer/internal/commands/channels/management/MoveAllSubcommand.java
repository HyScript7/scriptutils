package io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels.management;

import io.github.hyscript7.scriptutils.domain.discord.commands.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.attribute.ICategorizableChannel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;

public class MoveAllSubcommand extends Subcommand {
    public MoveAllSubcommand() {
        super(CommandMeta.builder().name("moveall").description("Moves all channels from one category to another")
                .addOption("source", "The source category to move channels from (null = uncategorized channels)", OptionMeta.Type.CHANNEL, false, null)
                .addOption("target", "The target category to move channels to (null = remove category)", OptionMeta.Type.CHANNEL, false, null)
                .build());
    }

    private static final Predicate<GuildChannel> isNotACategory = channel -> !(channel instanceof Category);
    private static final Predicate<GuildChannel> isCategorizable = channel -> channel instanceof ICategorizableChannel;
    private static final Function<GuildChannel, ICategorizableChannel> mapToCategorizable = channel -> (ICategorizableChannel) channel;

    @Override
    public void execute(CommandContext context) {
        @Nullable Category sourceCategory = (Category) context.getOption("source");
        @Nullable Category targetCategory = (Category) context.getOption("target");

        GuildContext guild;
        if (context.getGuild().isEmpty()) {
            context.send("This command can only be ran in a guild!", true);
            return;
        }
        guild = context.getGuild().get();

        if(!guild.memberHasPermission(context.getAuthorId(),
                Permission.MANAGE_CHANNEL.getRawValue())) {
            context.send("You do not have permission to use this command!", true);
            return;
        }

        if (sourceCategory == null && targetCategory == null) {
            context.send("You must specify at least one category! What are you even trying to do here? 🤨", true);
            return;
        }

        if (sourceCategory != null && isNotACategory.test(sourceCategory)) {
            context.send("The source must be a category!", true);
            return;
        }

        if (targetCategory != null && isNotACategory.test(targetCategory)) {
            context.send("The target must be a category!", true);
            return;
        }

        if (sourceCategory != null && targetCategory != null &&
                sourceCategory.getIdLong() == targetCategory.getIdLong()) {
            context.send("The source and target categories cannot be the same!", true);
            return;
        }

        context.defer(true);

        List<ICategorizableChannel> channelsToMove;

        if (sourceCategory == null) {
            // Get all uncategorized channels
            channelsToMove = targetCategory.getGuild().getChannels().stream()
                    .filter(isNotACategory)
                    .filter(isCategorizable)
                    .map(mapToCategorizable)
                    .filter(channel -> channel.getParentCategory() == null)
                    .toList();
        } else {
            // Get all channels in the source category
            channelsToMove = sourceCategory.getChannels().stream().filter(isCategorizable).map(mapToCategorizable).toList();
        }

        if (channelsToMove.isEmpty()) {
            String sourceDesc = sourceCategory == null ? "without a category" : "in <#" + sourceCategory.getIdLong() + ">";
            context.send("No channels found " + sourceDesc + "!");
            return;
        }

        int movedCount = 0;
        // This should essentially silently ignore all errors.
        // Probably don't need to throw or alert the user, since channels are clearly visible.
        // And if the user can't notice, they have a bigger problem than the bot not moving the channels.
        // Either with eyesight, or with the amount of channels on their guild.
        for (ICategorizableChannel channel : channelsToMove) {
            if (targetCategory == null) {
                // Remove category
                channel.getManager().setParent(null).queue(
                        success -> {},
                        error -> {}
                );
            } else {
                // Move to target category
                channel.getManager().setParent(targetCategory).queue(
                        success -> {},
                        error -> {}
                );
            }
            movedCount++;
        }

        String sourceDesc = sourceCategory == null ? "without a category" : "from <#" + sourceCategory.getIdLong() + ">";
        String targetDesc = targetCategory == null ? "removed from categories" : "to <#" + targetCategory.getIdLong() + ">";

        context.send("Moving " + movedCount + " channel(s) " + sourceDesc + " " + targetDesc + "!");
    }
}