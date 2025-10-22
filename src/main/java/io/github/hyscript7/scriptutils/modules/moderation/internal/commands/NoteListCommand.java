package io.github.hyscript7.scriptutils.modules.moderation.internal.commands;

import java.time.ZoneOffset;
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
public class NoteListCommand extends Subcommand {

    private final NoteService noteService;

    public NoteListCommand(NoteService noteService) {
        super(new CommandMeta("list", "Lists the notes on a member.", List.of(
                new OptionMeta("member", "The member to list warnings for", OptionMeta.Type.USER, true),
                new OptionMeta("page", "The page to list", OptionMeta.Type.INTEGER, false, 1))));
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

        // TODO: Paginate. Discord has a message limit. If we would hit that, we need to
        // paginate.
        // int page = (Integer) context.getOption("page");

        List<Note> notes = noteService.getNotesForMember(guild.getGuildId(), user.getIdLong());
        StringBuilder builder = new StringBuilder();
        for (Note note : notes) {
            builder.append("\n- #").append(note.getId()).append(" added by <@").append(note.getModeratorId())
                    .append("> on <t:").append(note.getCreatedAt().toEpochSecond(ZoneOffset.UTC))
                    .append(":f> and last updated <t:").append(note.getUpdatedAt().toEpochSecond(ZoneOffset.UTC))
                    .append(":R>");
        }
        context.send("Notes for " + user.getAsMention() + ": \n" + builder.toString().trim(), true);
    }

}
