package io.github.hyscript7.scriptutils.modules.multichat.internal.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MultichatBinding {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private Long channelId; // One channel can only be part of one group
    private Long guildId;
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private MultichatGroup group;
}
