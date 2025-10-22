package io.github.hyscript7.scriptutils.modules.moderation.internal.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
public class Warning {
    @Id
    @GeneratedValue(
        strategy = GenerationType.AUTO
    )
    private Long id;
    
    private Long guildId;

    private Long userId;

    private Long moderatorId;

    private String reason;

    private LocalDateTime date;
}
