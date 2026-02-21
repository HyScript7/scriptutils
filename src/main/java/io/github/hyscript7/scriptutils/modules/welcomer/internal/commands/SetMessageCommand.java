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
public class SetMessageCommand extends Subcommand {
    
    private final WelcomerService welcomerService;
    
    public SetMessageCommand(WelcomerService welcomerService) {
        super(new CommandMeta("setmessage", "Sets the welcome message template.", List.of(
                new OptionMeta("message", "The welcome message. Use {username}, {mention}, {guild} as placeholders.", 
                        OptionMeta.Type.STRING, true)
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
        
        String message = (String) context.getOption("message");
        
        welcomerService.setWelcomeMessage(guild.getGuildId(), message);
        
        context.send("Welcome message updated to:\n```\n" + message + "\n```\n" +
                "Available placeholders: `{username}`, `{mention}`, `{guild}`", true);
    }
}
