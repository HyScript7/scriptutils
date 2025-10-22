package io.github.hyscript7.scriptutils.modules.moderation.internal.services;

import java.time.LocalDateTime;
import java.util.List;

import io.github.hyscript7.scriptutils.modules.moderation.internal.repositories.WarningRepository;
import org.springframework.stereotype.Service;

import io.github.hyscript7.scriptutils.modules.moderation.internal.models.Warning;

@Service
public class WarningService {

    private final WarningRepository warningRepository;

    WarningService(WarningRepository warningRepository) {
        this.warningRepository = warningRepository;
    }

    public Warning issueNewWarning(long guildId, long userId, long moderatorId, String reason, LocalDateTime date) {
        Warning warning = Warning.builder().guildId(guildId).userId(userId).moderatorId(moderatorId).reason(reason)
                .date(date).build();
        return warningRepository.save(warning);
    }

    public List<Warning> getWarningsForUser(long guildId, long userId) {
        return warningRepository.findByGuildIdAndUserId(guildId, userId);
    }

}
