package io.github.hyscript7.scriptutils.modules.welcomer.internal.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class GuildWelcomerSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(unique = true)
    private Long guildId;
    
    private Boolean enabled;
    
    @Column(length = 2000)
    private String welcomeMessage;

    private Long channelId; // Null if sending via DM
    
    private Boolean sendViaDm; // If true, send to member's DM instead of channel
}
