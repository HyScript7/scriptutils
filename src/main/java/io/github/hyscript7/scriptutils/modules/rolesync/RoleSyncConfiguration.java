package io.github.hyscript7.scriptutils.modules.rolesync;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.RoleSyncCommandGroup;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.bindings.CreateBindingCommand;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.bindings.DeleteBindingCommand;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.bindings.ListBindingCommand;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.bindings.RoleSyncBindingSubcommandGroup;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.groups.AddRoleCommand;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.groups.CreateGroupCommand;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.groups.DeleteGroupCommand;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.groups.ListGroupCommand;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.groups.ListRolesCommand;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.groups.RemoveRoleCommand;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.commands.groups.RoleSyncGroupSubcommandGroup;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.listeners.RoleChangeListener;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.services.RoleSyncGroupService;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.services.RoleSyncRoleService;

@Configuration
public class RoleSyncConfiguration {

    @Bean
    CreateBindingCommand createBindingCommand(RoleSyncRoleService roleService, RoleSyncGroupService groupService) {
        return new CreateBindingCommand(roleService, groupService);
    }

    @Bean
    DeleteBindingCommand deleteBindingCommand(RoleSyncRoleService roleService) {
        return new DeleteBindingCommand(roleService);
    }

    @Bean
    ListBindingCommand listBindingCommand(RoleSyncRoleService roleService) {
        return new ListBindingCommand(roleService);
    }

    @Bean
    RoleSyncBindingSubcommandGroup roleSyncBindingSubcommandGroup(CreateBindingCommand createBindingCommand,
            DeleteBindingCommand deleteBindingCommand, ListBindingCommand listBindingCommand) {
        RoleSyncBindingSubcommandGroup subcommandGroup = new RoleSyncBindingSubcommandGroup();
        subcommandGroup.addSubcommand(createBindingCommand);
        subcommandGroup.addSubcommand(deleteBindingCommand);
        subcommandGroup.addSubcommand(listBindingCommand);
        return subcommandGroup;
    }

    @Bean
    AddRoleCommand addRoleCommand(RoleSyncRoleService roleService, RoleSyncGroupService groupService) {
        return new AddRoleCommand(roleService, groupService);
    }

    @Bean
    RemoveRoleCommand removeRoleCommand(RoleSyncRoleService roleService, RoleSyncGroupService groupService) {
        return new RemoveRoleCommand(groupService, roleService);
    }

    @Bean
    ListRolesCommand listRolesCommand(RoleSyncGroupService groupService) {
        return new ListRolesCommand(groupService);
    }

    @Bean
    CreateGroupCommand createGroupCommand(RoleSyncGroupService groupService) {
        return new CreateGroupCommand(groupService);
    }

    @Bean
    DeleteGroupCommand deleteGroupCommand(RoleSyncGroupService groupService) {
        return new DeleteGroupCommand(groupService);
    }

    @Bean
    ListGroupCommand listGroupCommand(RoleSyncGroupService groupService) {
        return new ListGroupCommand(groupService);
    }

    @Bean
    RoleSyncGroupSubcommandGroup roleSyncGroupSubcommandGroup(CreateGroupCommand createGroupCommand,
            DeleteGroupCommand deleteGroupCommand, ListGroupCommand listGroupCommand,
            AddRoleCommand addRoleCommand, RemoveRoleCommand removeRoleCommand, ListRolesCommand listRolesCommand) {
        RoleSyncGroupSubcommandGroup subcommandGroup = new RoleSyncGroupSubcommandGroup();
        subcommandGroup.addSubcommand(createGroupCommand);
        subcommandGroup.addSubcommand(deleteGroupCommand);
        subcommandGroup.addSubcommand(listGroupCommand);
        subcommandGroup.addSubcommand(addRoleCommand);
        subcommandGroup.addSubcommand(removeRoleCommand);
        subcommandGroup.addSubcommand(listRolesCommand);
        return subcommandGroup;
    }

    @Bean
    RoleSyncCommandGroup roleSyncCommandGroup(RoleSyncBindingSubcommandGroup bindingSubcommandGroup,
            RoleSyncGroupSubcommandGroup groupSubcommandGroup) {
        RoleSyncCommandGroup commandGroup = new RoleSyncCommandGroup();
        commandGroup.addSubcommandGroup(bindingSubcommandGroup);
        commandGroup.addSubcommandGroup(groupSubcommandGroup);
        return commandGroup;
    }

    @Bean
    RoleChangeListener roleChangeListener(RoleSyncRoleService roleService) {
        return new RoleChangeListener(roleService);
    }

    @Bean
    RoleSyncModule roleSyncModule(RoleSyncCommandGroup roleSyncCommandGroup, RoleChangeListener roleChangeListener) {
        RoleSyncModule module = new RoleSyncModule();
        module.addCommand(roleSyncCommandGroup);
        module.addEventListener(roleChangeListener);
        return module;
    }

}
