package io.github.hyscript7.scriptutils.modules.multichat.internal.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MultichatGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Long ownerId;
    private String description;
    @OneToMany(mappedBy = "group", orphanRemoval = true, cascade = { CascadeType.ALL })
    private List<MultichatBinding> channels;
}
