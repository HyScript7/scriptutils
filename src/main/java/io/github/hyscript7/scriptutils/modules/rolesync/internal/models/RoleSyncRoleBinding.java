package io.github.hyscript7.scriptutils.modules.rolesync.internal.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// Represents a single target role on a guild under a role group umbrella
public class RoleSyncRoleBinding {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long guildId;
    @Column(unique = true)
    private Long roleId;
    @ManyToOne
    @JoinColumn(name = "group_role_id", nullable = false)
    private RoleSyncGroupRole role;
}
