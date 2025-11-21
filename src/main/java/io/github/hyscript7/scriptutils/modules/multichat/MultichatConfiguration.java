package io.github.hyscript7.scriptutils.modules.multichat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.hyscript7.scriptutils.modules.multichat.internal.commands.GroupCreateCommand;
import io.github.hyscript7.scriptutils.modules.multichat.internal.commands.GroupDeleteCommand;
import io.github.hyscript7.scriptutils.modules.multichat.internal.commands.GroupJoinCommand;
import io.github.hyscript7.scriptutils.modules.multichat.internal.commands.GroupLeaveCommand;
import io.github.hyscript7.scriptutils.modules.multichat.internal.commands.GroupListLinkedCommand;
import io.github.hyscript7.scriptutils.modules.multichat.internal.commands.GroupListOwnCommand;
import io.github.hyscript7.scriptutils.modules.multichat.internal.commands.GroupListSubcommandGroup;
import io.github.hyscript7.scriptutils.modules.multichat.internal.commands.MultichatCommandGroup;
import io.github.hyscript7.scriptutils.modules.multichat.internal.listeners.OnMessageListener;
import io.github.hyscript7.scriptutils.modules.multichat.internal.services.GroupService;

@Configuration
public class MultichatConfiguration {

    @Bean
    GroupCreateCommand groupCreateCommand(GroupService groupService) {
        return new GroupCreateCommand(groupService);
    }

    @Bean
    GroupDeleteCommand groupDeleteCommand(GroupService groupService) {
        return new GroupDeleteCommand(groupService);
    }

    @Bean
    GroupListOwnCommand groupListOwnCommand(GroupService groupService) {
        return new GroupListOwnCommand(groupService);
    }

    @Bean
    GroupListLinkedCommand groupListLinkedCommand(GroupService groupService) {
        return new GroupListLinkedCommand(groupService);
    }

    @Bean
    GroupListSubcommandGroup groupListSubcommandGroup(GroupListOwnCommand groupListOwnCommand,
            GroupListLinkedCommand groupListLinkedCommand) {
        GroupListSubcommandGroup subcommandGroup = new GroupListSubcommandGroup();
        subcommandGroup.addSubcommand(groupListOwnCommand);
        subcommandGroup.addSubcommand(groupListLinkedCommand);
        return subcommandGroup;
    }

    @Bean
    GroupJoinCommand groupJoinCommand(GroupService groupService) {
        return new GroupJoinCommand(groupService);
    }

    @Bean
    GroupLeaveCommand groupLeaveCommand(GroupService groupService) {
        return new GroupLeaveCommand(groupService);
    }

    @Bean
    MultichatCommandGroup multichatCommandGroup(GroupCreateCommand groupCreateCommand,
            GroupDeleteCommand groupDeleteCommand, GroupListSubcommandGroup groupListSubcommandGroup,
            GroupJoinCommand groupJoinCommand, GroupLeaveCommand groupLeaveCommand) {
        MultichatCommandGroup commandGroup = new MultichatCommandGroup();
        commandGroup.addSubcommand(groupCreateCommand);
        commandGroup.addSubcommand(groupDeleteCommand);
        commandGroup.addSubcommandGroup(groupListSubcommandGroup);
        commandGroup.addSubcommand(groupJoinCommand);
        commandGroup.addSubcommand(groupLeaveCommand);
        return commandGroup;
    }

    @Bean
    OnMessageListener onMessageListener(GroupService groupService) {
        return new OnMessageListener(groupService);
    }

    @Bean
    MultichatModule multichatModule(MultichatCommandGroup multichatCommandGroup, OnMessageListener onMessageListener) {
        MultichatModule module = new MultichatModule();
        module.addCommand(multichatCommandGroup);
        module.addEventListener(onMessageListener);
        return module;
    }

}
