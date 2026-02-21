package io.github.hyscript7.scriptutils.modules.welcomer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.hyscript7.scriptutils.modules.welcomer.internal.commands.EnableCommand;
import io.github.hyscript7.scriptutils.modules.welcomer.internal.commands.SetChannelCommand;
import io.github.hyscript7.scriptutils.modules.welcomer.internal.commands.SetMessageCommand;
import io.github.hyscript7.scriptutils.modules.welcomer.internal.commands.StatusCommand;
import io.github.hyscript7.scriptutils.modules.welcomer.internal.commands.WelcomerCommandGroup;
import io.github.hyscript7.scriptutils.modules.welcomer.internal.listeners.MemberJoinListener;
import io.github.hyscript7.scriptutils.modules.welcomer.internal.services.WelcomerService;

@Configuration
public class WelcomerConfiguration {
    
    @Bean
    SetMessageCommand setMessageCommand(WelcomerService welcomerService) {
        return new SetMessageCommand(welcomerService);
    }
    
    @Bean
    SetChannelCommand setChannelCommand(WelcomerService welcomerService) {
        return new SetChannelCommand(welcomerService);
    }
    
    @Bean
    EnableCommand enableCommand(WelcomerService welcomerService) {
        return new EnableCommand(welcomerService);
    }
    
    @Bean
    StatusCommand statusCommand(WelcomerService welcomerService) {
        return new StatusCommand(welcomerService);
    }
    
    @Bean
    WelcomerCommandGroup welcomerCommandGroup(SetMessageCommand setMessageCommand, 
            SetChannelCommand setChannelCommand, EnableCommand enableCommand, 
            StatusCommand statusCommand) {
        WelcomerCommandGroup commandGroup = new WelcomerCommandGroup();
        commandGroup.addSubcommand(setMessageCommand);
        commandGroup.addSubcommand(setChannelCommand);
        commandGroup.addSubcommand(enableCommand);
        commandGroup.addSubcommand(statusCommand);
        return commandGroup;
    }
    
    @Bean
    MemberJoinListener memberJoinListener(WelcomerService welcomerService) {
        return new MemberJoinListener(welcomerService);
    }
    
    @Bean
    WelcomerModule welcomerModule(WelcomerCommandGroup welcomerCommandGroup, 
            MemberJoinListener memberJoinListener) {
        WelcomerModule module = new WelcomerModule();
        module.addCommand(welcomerCommandGroup);
        module.addEventListener(memberJoinListener);
        return module;
    }
}
