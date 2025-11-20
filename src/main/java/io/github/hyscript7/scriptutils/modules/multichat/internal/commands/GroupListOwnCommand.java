package io.github.hyscript7.scriptutils.modules.multichat.internal.commands;

import io.github.hyscript7.scriptutils.modules.multichat.internal.models.MultichatGroup;
import io.github.hyscript7.scriptutils.modules.multichat.internal.services.GroupService;

import java.util.List;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;

@Component
public class GroupListOwnCommand extends Subcommand {

    private final GroupService groupService;

    public GroupListOwnCommand(GroupService groupService) {
        super(new CommandMeta("own", "Lists all groups you own."));
        this.groupService = groupService;
    }

    @Override
    public void execute(CommandContext context) {
        StringBuilder stringBuilder = new StringBuilder("Your groups:\n");
        // TODO: Paginate in case someone has more groups than we can fit in 2000
        // characters.
        List<MultichatGroup> groups = groupService.getGroupsBelongingToUser(context.getAuthorId());
        if (groups.isEmpty()) {
            stringBuilder.append("None");
            context.send(stringBuilder.toString(), true);
            return;
        }
        groups.stream().forEach(group -> stringBuilder.append("- ")
                .append(group.getName()).append(" - ").append(group.getDescription()).append("\n"));
        context.send(stringBuilder.toString().trim(), true);
    }

}
