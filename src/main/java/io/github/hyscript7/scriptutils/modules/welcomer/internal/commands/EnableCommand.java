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

@Component
public class EnableCommand extends Subcommand {
    
    private final WelcomerService welcomerService;
    
    public EnableCommand(WelcomerService welcomerService) {
        super(new CommandMeta("enable", "Enables or disables the welcomer module.", List.of(
                new OptionMeta("enabled", "Whether to enable the welcomer", 
                        OptionMeta.Type.BOOLEAN, true)
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
        
        boolean enabled = (Boolean) context.getOption("enabled");
        
        welcomerService.setEnabled(guild.getGuildId(), enabled);
        
        context.send("Welcomer has been " + (enabled ? "enabled" : "disabled") + ".", true);
    }
}
