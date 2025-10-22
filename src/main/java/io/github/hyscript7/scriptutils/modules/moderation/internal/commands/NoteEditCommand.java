package io.github.hyscript7.scriptutils.modules.moderation.internal.commands;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;
import io.github.hyscript7.scriptutils.modules.moderation.internal.models.Note;
import io.github.hyscript7.scriptutils.modules.moderation.internal.services.NoteService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

@Component
public class NoteEditCommand extends Subcommand {

    private final NoteService noteService;

    public NoteEditCommand(NoteService noteService) {
        super(new CommandMeta("edit", "Edits an existing note on a member.", List.of(
                new OptionMeta("member", "The member to modify a note on.", OptionMeta.Type.USER, true),
                new OptionMeta("note", "New note content", OptionMeta.Type.STRING, true),
                new OptionMeta("noteid", "The ID of the note to edit", OptionMeta.Type.INTEGER, true))));
        this.noteService = noteService;
    }

    @Override
    public void execute(CommandContext context) {
        Optional<GuildContext> guildOptional = context.getGuild();
        if (guildOptional.isEmpty()) {
            context.send("You must be in a guild to use this command!", true);
            return;
        }
        GuildContext guild = guildOptional.get();

        boolean hasModPerms = guild.memberHasPermission(context.getAuthorId(),
                Permission.MODERATE_MEMBERS.getRawValue());

        if (!hasModPerms) {
            context.send("You do not have permission to use this command!", true);
            return;
        }

        User user = (User) context.getOption("member");
        String noteContent = (String) context.getOption("note");
        long noteId = (Long) context.getOption("noteid");

        Optional<Note> noteOptional = noteService.getNoteById(noteId);

        String notFoundMessage = "Could not find note with ID " + noteId;

        if (noteOptional.isEmpty()) {
            context.send(notFoundMessage, true);
            return;
        }

        Note note = noteOptional.get();

        // If the note does not belong to the guild the command was run in
        if (note.getGuildId() != guild.getGuildId()) {
            context.send(notFoundMessage, true);
            return;
        }

        if (note.getUserId() != user.getIdLong()) {
            context.send("Note with ID " + noteId + " does not belong to " + user.getAsMention(), true);
            return;
        }

        note.setContent(noteContent);

        noteService.updateNote(note);
        context.send("Successfully updated note for " + user.getAsMention(), true);
    }

}
