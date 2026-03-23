package io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels.permissions;

import io.github.hyscript7.scriptutils.domain.discord.commands.*;
import io.github.hyscript7.scriptutils.modules.developer.internal.ChannelAccessType;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.jetbrains.annotations.Nullable;

public class SetPermissionsSubcommand extends Subcommand {
    public SetPermissionsSubcommand() {
        super(CommandMeta.builder().name("set").description("Sets channel permissions for a role")
                .addOption("channel", "The channel to set permissions for", OptionMeta.Type.CHANNEL, true)
                .addOption("access", "The type of access to grant", OptionMeta.Type.STRING, true)
                .addOption("role", "The role to set permissions for (defaults to @everyone)", OptionMeta.Type.ROLE, false, null)
                .build());
    }

    @Override
    public void execute(CommandContext context) {
        Channel channel = (Channel) context.getOption("channel");
        String accessString = (String) context.getOption("access");
        @Nullable Role role = (Role) context.getOption("role");

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

        if (!(channel instanceof GuildChannel guildChannel)) {
            context.send("The specified channel is not a guild channel!", true);
            return;
        }

        ChannelAccessType accessType = parseAccessType(accessString);
        if (accessType == null) {
            context.send("Invalid access type! Valid types: `allow`, `read`, `deny`", true);
            return;
        }

        context.defer(true);

        ChannelContext channelContext = guild.getChannel(channel.getIdLong()).orElse(null);
        if (channelContext == null) {
            context.send("Channel not found in this guild!", true);
            return;
        }

        // If no role specified, use @everyone
        Role targetRole = role != null ? role : guildChannel.getGuild().getPublicRole();

        applyPermissions(guildChannel, targetRole, accessType);

        String roleMention = role != null ? "<@&" + role.getIdLong() + ">" : "@everyone";
        context.send("Set permissions for " + roleMention + " in channel <#" + channel.getIdLong() + "> to `" + accessType.name().toLowerCase() + "`!");
    }

    private ChannelAccessType parseAccessType(String accessString) {
        String cleaned = accessString.toLowerCase().trim();

        return switch (cleaned) {
            case "allow", "write", "full" -> ChannelAccessType.ALLOW;
            case "read", "view", "readonly" -> ChannelAccessType.READ;
            case "deny", "none", "block" -> ChannelAccessType.DENY;
            default -> null;
        };
    }

    private void applyPermissions(GuildChannel channel, Role role, ChannelAccessType accessType) {
        switch (accessType) {
            case ALLOW -> {
                // Full access: can view and send messages
                channel.getPermissionContainer().upsertPermissionOverride(role)
                        .setAllowed(
                                Permission.VIEW_CHANNEL,
                                Permission.MESSAGE_SEND,
                                Permission.MESSAGE_SEND_IN_THREADS,
                                Permission.CREATE_PUBLIC_THREADS,
                                Permission.MESSAGE_EMBED_LINKS,
                                Permission.MESSAGE_ATTACH_FILES,
                                Permission.MESSAGE_ADD_REACTION,
                                Permission.MESSAGE_HISTORY
                        )
                        .queue();
            }
            case READ -> {
                // Read-only access: can view but not send
                channel.getPermissionContainer().upsertPermissionOverride(role)
                        .setAllowed(
                                Permission.VIEW_CHANNEL,
                                Permission.MESSAGE_HISTORY
                        )
                        .setDenied(
                                Permission.MESSAGE_SEND,
                                Permission.MESSAGE_SEND_IN_THREADS,
                                Permission.CREATE_PUBLIC_THREADS,
                                Permission.MESSAGE_ADD_REACTION
                        )
                        .queue();
            }
            case DENY -> {
                // No access: cannot view the channel at all
                channel.getPermissionContainer().upsertPermissionOverride(role)
                        .setDenied(
                                Permission.VIEW_CHANNEL,
                                Permission.MESSAGE_SEND,
                                Permission.MESSAGE_HISTORY
                        )
                        .queue();
            }
        }
    }
}
