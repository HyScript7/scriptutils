package io.github.hyscript7.scriptutils.modules.rolesync.internal.listeners;

import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.modules.rolesync.internal.models.RoleSyncRoleBinding;
import io.github.hyscript7.scriptutils.modules.rolesync.internal.services.RoleSyncRoleService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

// Ah yes, welcome to Lambda hell.
// Hope you enjoy your stay.
@Component
@Slf4j
public class RoleChangeListener extends ListenerAdapter {

    private final RoleSyncRoleService roleSyncRoleService;

    public RoleChangeListener(RoleSyncRoleService roleSyncRoleService) {
        this.roleSyncRoleService = roleSyncRoleService;
    }

    @Override
    @Transactional
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        for (Role role : event.getRoles()) {
            Optional<RoleSyncRoleBinding> binding = roleSyncRoleService.getRoleBinding(event.getGuild().getIdLong(), role.getIdLong());
            if (binding.isEmpty()) continue;
            binding.get().getRole().getBindings().stream().filter(rb -> rb.getId() != binding.get().getId()).forEach(
                rb -> {
                    log.debug("Performing sync for {}@{} -> {}@{} (grant)", event.getUser(), role.getIdLong(), rb.getRoleId(), rb.getGuildId());
                    Guild guild = event.getJDA().getGuildById(rb.getGuildId());
                    if (guild == null) {
                        log.warn("Dropping invalid role sync connection of {}@{} -> {}@{}!", binding.get().getRoleId(), binding.get().getGuildId(), rb.getRoleId(), rb.getGuildId());
                        roleSyncRoleService.removeRoleFromGroup(rb);
                        return;
                    }
                    Member guildMember = guild.getMember(event.getUser());
                    if (guildMember == null) {
                        return;
                    }
                    Role guildRole = guild.getRoleById(rb.getRoleId());
                    if (guildRole == null) {
                        log.warn("Dropping invalid role sync connection of {}@{} -> {}@{}!", binding.get().getRoleId(), binding.get().getGuildId(), rb.getRoleId(), rb.getGuildId());
                        roleSyncRoleService.removeRoleFromGroup(rb);
                        return;
                    }
                    guild.addRoleToMember(event.getUser(), guildRole).queue();
                }
            );
        }
    }

    @Override
    @Transactional
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        for (Role role : event.getRoles()) {
            Optional<RoleSyncRoleBinding> binding = roleSyncRoleService.getRoleBinding(event.getGuild().getIdLong(), role.getIdLong());
            if (binding.isEmpty()) continue;
            binding.get().getRole().getBindings().stream().filter(rb -> rb.getId() != binding.get().getId()).forEach(
                rb -> {
                    log.debug("Performing sync for {}@{} -> {}@{} (revoke)", event.getUser(), role.getIdLong(), rb.getRoleId(), rb.getGuildId());
                    Guild guild = event.getJDA().getGuildById(rb.getGuildId());
                    if (guild == null) {
                        log.warn("Dropping invalid role sync connection of {}@{} -> {}@{}!", binding.get().getRoleId(), binding.get().getGuildId(), rb.getRoleId(), rb.getGuildId());
                        roleSyncRoleService.removeRoleFromGroup(rb);
                        return;
                    }
                    Role guildRole = guild.getRoleById(rb.getRoleId());
                    if (guildRole == null) {
                        log.warn("Dropping invalid role sync connection of {}@{} -> {}@{}!", binding.get().getRoleId(), binding.get().getGuildId(), rb.getRoleId(), rb.getGuildId());
                        roleSyncRoleService.removeRoleFromGroup(rb);
                        return;
                    }
                    guild.removeRoleFromMember(event.getUser(), guildRole).queue();
                }
            );
        }
    }

    @Override
    public void onRoleDelete(RoleDeleteEvent event) {
        // Each role can only be in one group to prevent complicated architecture which
        // would span a bazillion guilds.
        // As such, we can just pass it to the consumer-like method.
        log.info("Noticed role deleted!");
        roleSyncRoleService.getRoleBinding(event.getGuild().getIdLong(), event.getRole().getIdLong())
                .ifPresent(roleSyncRoleService::removeRoleFromGroup);
    }
}
