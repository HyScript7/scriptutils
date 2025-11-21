package io.github.hyscript7.scriptutils.modules.rolesync.internal.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "owner_id", "name" }))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// Groups together roles to be synced
public class RoleSyncRoleGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long ownerId;
    private String name;
    private String description;
    @OneToMany(mappedBy = "group", orphanRemoval = true, cascade = { CascadeType.ALL })
    private List<RoleSyncGroupRole> roles;
}
