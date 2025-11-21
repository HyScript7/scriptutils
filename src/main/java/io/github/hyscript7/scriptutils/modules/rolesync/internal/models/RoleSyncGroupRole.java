package io.github.hyscript7.scriptutils.modules.rolesync.internal.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Represents a collection of roles which are equivalent
public class RoleSyncGroupRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private RoleSyncRoleGroup group;
    private String name;
    private String description;
    @OneToMany(mappedBy = "role", orphanRemoval = true, cascade = { CascadeType.ALL })
    private List<RoleSyncRoleBinding> bindings;
}
