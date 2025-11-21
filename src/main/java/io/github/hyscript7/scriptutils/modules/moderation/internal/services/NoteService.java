package io.github.hyscript7.scriptutils.modules.moderation.internal.services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import io.github.hyscript7.scriptutils.modules.moderation.internal.repositories.NoteRepository;
import org.springframework.stereotype.Service;

import io.github.hyscript7.scriptutils.modules.moderation.internal.models.Note;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note attachNote(long guildId, long userId, long moderatorId, String content) {
        Note note = Note.builder().guildId(guildId).userId(userId).moderatorId(moderatorId).content(content)
                .createdAt(LocalDateTime.now(ZoneOffset.UTC)).updatedAt(LocalDateTime.now(ZoneOffset.UTC)).build();
        return noteRepository.save(note);
    }

    public Note updateNote(Note note) {
        note.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        return noteRepository.save(note);
    }

    public void deleteNote(Note note) {
        noteRepository.delete(note);
    }

    public List<Note> getNotesForMember(long guildId, long userId) {
        return noteRepository.findByGuildIdAndUserId(guildId, userId);
    }

    public Optional<Note> getNoteById(long id) {
        return noteRepository.findById(id);
    }

}
