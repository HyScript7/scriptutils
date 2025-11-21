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
public class NoteAddCommand extends Subcommand {

    private final NoteService noteService;

    public NoteAddCommand(NoteService noteService) {
        super(new CommandMeta("add", "Add a new mod note to a member.", List.of(
            new OptionMeta("member", "The member to add the note to", OptionMeta.Type.USER, true),
            new OptionMeta("note", "The note to add", OptionMeta.Type.STRING, true)
        )));
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

        Note note = noteService.attachNote(guild.getGuildId(), user.getIdLong(), context.getAuthorId(), noteContent);
        context.send("Successfully added note to " + user.getAsMention() + " with id " + note.getId(), true);
    }
    
}
