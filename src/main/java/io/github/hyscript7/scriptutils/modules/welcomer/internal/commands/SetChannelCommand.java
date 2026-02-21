package io.github.hyscript7.scriptutils.modules.welcomer.internal.commands;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;
import io.github.hyscript7.scriptutils.modules.welcomer.internal.services.WelcomerService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;

@Component
public class SetChannelCommand extends Subcommand {
    
    private final WelcomerService welcomerService;
    
    public SetChannelCommand(WelcomerService welcomerService) {
        super(new CommandMeta("setchannel", "Sets the channel to send welcome messages to.", List.of(
                new OptionMeta("channel", "The channel to send welcome messages to (leave empty for DM)", 
                        OptionMeta.Type.CHANNEL, false, null)
        )));
        this.welcomerService = welcomerService;
    }
    
    @Override
    public void execute(CommandContext context) {
        Optional<GuildContext> guildOptional = context.getGuild();
        if (guildOptional.isEmpty()) {
            context.send("You must be in a guild to use this command!", true);
            return;
        }
        GuildContext guild = guildOptional.get();
        
        if (!guild.memberHasPermission(context.getAuthorId(), Permission.MANAGE_SERVER.getRawValue())) {
            context.send("You must have the `MANAGE SERVER` permission to use this command!", true);
            return;
        }

        GuildChannelUnion channel = (GuildChannelUnion) context.getOption("channel");
        if (channel != null) {
            welcomerService.setChannel(guild.getGuildId(), channel.getIdLong());
            context.send("Welcome messages will now be sent to <#" + channel.getIdLong() + ">", true);
        } else {
            // No channel provided, set to DM
            welcomerService.setSendViaDm(guild.getGuildId(), true);
            context.send("Welcome messages will now be sent via DM to new members.", true);
        }
    }
}
