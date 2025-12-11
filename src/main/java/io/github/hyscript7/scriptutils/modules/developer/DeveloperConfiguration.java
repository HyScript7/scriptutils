package io.github.hyscript7.scriptutils.modules.developer;

import io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels.ChannelCommandGroup;
import io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels.management.CreateChannelSubcommand;
import io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels.management.DeleteChannelSubcommand;
import io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels.management.EditChannelSubcommand;
import io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels.management.MoveAllSubcommand;
import io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels.permissions.ChannelPermissionSubcommandGroup;
import io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels.permissions.ResetPermissionsSubcommand;
import io.github.hyscript7.scriptutils.modules.developer.internal.commands.channels.permissions.SetPermissionsSubcommand;
import io.github.hyscript7.scriptutils.modules.developer.internal.commands.roles.RoleCommandGroup;
import io.github.hyscript7.scriptutils.modules.developer.internal.commands.roles.management.CreateRoleSubcommand;
import io.github.hyscript7.scriptutils.modules.developer.internal.commands.roles.management.DeleteRoleSubcommand;
import io.github.hyscript7.scriptutils.modules.developer.internal.commands.roles.management.EditRoleSubcommand;
import io.github.hyscript7.scriptutils.modules.developer.internal.commands.roles.memberships.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeveloperConfiguration {

    @Bean
    CreateChannelSubcommand createChannelSubcommand() {
        return new CreateChannelSubcommand();
    }

    @Bean
    DeleteChannelSubcommand deleteChannelSubcommand() {
        return new DeleteChannelSubcommand();
    }

    @Bean
    EditChannelSubcommand editChannelSubcommand() {
        return new EditChannelSubcommand();
    }

    @Bean
    ResetPermissionsSubcommand resetPermissionsSubcommand() {
        return new ResetPermissionsSubcommand();
    }

    @Bean
    SetPermissionsSubcommand setPermissionsSubcommand() {
        return new SetPermissionsSubcommand();
    }

    @Bean
    ChannelPermissionSubcommandGroup channelPermissionSubcommandGroup(ResetPermissionsSubcommand resetPermissionsSubcommand, SetPermissionsSubcommand setPermissionsSubcommand) {
        ChannelPermissionSubcommandGroup subcommandGroup = new ChannelPermissionSubcommandGroup();
        subcommandGroup.addSubcommand(resetPermissionsSubcommand);
        subcommandGroup.addSubcommand(setPermissionsSubcommand);
        return subcommandGroup;
    }

    @Bean
    MoveAllSubcommand moveAllSubcommand() {
        return new MoveAllSubcommand();
    }

    @Bean
    ChannelCommandGroup channelCommandGroup(CreateChannelSubcommand createChannelSubcommand, DeleteChannelSubcommand deleteChannelSubcommand, EditChannelSubcommand editChannelSubcommand, MoveAllSubcommand moveAllSubcommand, ChannelPermissionSubcommandGroup channelPermissionSubcommandGroup) {
        ChannelCommandGroup commandGroup = new ChannelCommandGroup();
        commandGroup.addSubcommand(createChannelSubcommand);
        commandGroup.addSubcommand(deleteChannelSubcommand);
        commandGroup.addSubcommand(editChannelSubcommand);
        commandGroup.addSubcommand(moveAllSubcommand);
        commandGroup.addSubcommandGroup(channelPermissionSubcommandGroup);
        return commandGroup;
    }

    @Bean
    CreateRoleSubcommand createRoleSubcommand() {
        return new CreateRoleSubcommand();
    }

    @Bean
    DeleteRoleSubcommand deleteRoleSubcommand() {
        return new DeleteRoleSubcommand();
    }

    @Bean
    EditRoleSubcommand editRoleSubcommand() {
        return new EditRoleSubcommand();
    }

    @Bean
    AddMemberSubcommand addMemberSubcommand() {
        return new AddMemberSubcommand();
    }

    @Bean
    RemoveMemberSubcommand removeMemberSubcommand() {
        return new RemoveMemberSubcommand();
    }

    @Bean
    AddAllMembersSubcommand addAllMembersSubcommand() {
        return new AddAllMembersSubcommand();
    }

    @Bean
    RemoveAllMembersSubcommand removeAllMembersSubcommand() {
        return new RemoveAllMembersSubcommand();
    }

    @Bean
    RoleMembershipSubcommandGroup roleMembershipSubcommandGroup(AddMemberSubcommand addMemberSubcommand, RemoveMemberSubcommand removeMemberSubcommand, AddAllMembersSubcommand addAllMembersSubcommand, RemoveAllMembersSubcommand removeAllMembersSubcommand) {
        RoleMembershipSubcommandGroup subcommandGroup = new RoleMembershipSubcommandGroup();
        subcommandGroup.addSubcommand(addMemberSubcommand);
        subcommandGroup.addSubcommand(removeMemberSubcommand);
        subcommandGroup.addSubcommand(addAllMembersSubcommand);
        subcommandGroup.addSubcommand(removeAllMembersSubcommand);
        return subcommandGroup;
    }

    @Bean
    RoleCommandGroup roleCommandGroup(CreateRoleSubcommand createRoleSubcommand, DeleteRoleSubcommand deleteRoleSubcommand, EditRoleSubcommand editRoleSubcommand, RoleMembershipSubcommandGroup roleMembershipSubcommandGroup) {
        RoleCommandGroup commandGroup = new RoleCommandGroup();
        commandGroup.addSubcommand(createRoleSubcommand);
        commandGroup.addSubcommand(deleteRoleSubcommand);
        commandGroup.addSubcommand(editRoleSubcommand);
        commandGroup.addSubcommandGroup(roleMembershipSubcommandGroup);
        return commandGroup;
    }

    @Bean
    DeveloperModule developerModule(ChannelCommandGroup channelCommandGroup, RoleCommandGroup roleCommandGroup) {
        DeveloperModule module = new DeveloperModule();
        module.addCommand(channelCommandGroup);
        module.addCommand(roleCommandGroup);
        return module;
    }
}
