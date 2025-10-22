package io.github.hyscript7.scriptutils.modules.moderation.internal.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.github.hyscript7.scriptutils.modules.moderation.internal.models.Note;

public interface NoteRepository extends CrudRepository<Note, Long> {
    List<Note> findByGuildIdAndUserId(long guildId, long userId);
}
