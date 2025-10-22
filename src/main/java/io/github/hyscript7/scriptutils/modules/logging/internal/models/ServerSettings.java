package io.github.hyscript7.scriptutils.modules.logging.internal.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ServerSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private Long guildId;
    @Column(nullable = true)
    private String defaultWebhookUrl;
    @Column(nullable = true)
    private String messageWebhookUrl;
    @Column(nullable = true)
    private String memberWebhookUrl;
    @Column(nullable = true)
    private String serverWebhookUrl;
    @Column(nullable = true)
    private String voiceWebhookUrl;
    @Column(nullable = true)
    private String moderationWebhookUrl;
    @Column(nullable = true)
    private String autoModerationWebhookUrl;
}
