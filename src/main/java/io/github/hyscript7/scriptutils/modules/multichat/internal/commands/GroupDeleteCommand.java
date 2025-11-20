package io.github.hyscript7.scriptutils.modules.multichat.internal.commands;

import java.util.List;
import java.util.Optional;

import io.github.hyscript7.scriptutils.modules.multichat.internal.models.MultichatGroup;
import io.github.hyscript7.scriptutils.modules.multichat.internal.services.GroupService;
import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;

@Component
public class GroupDeleteCommand extends Subcommand {

    private final GroupService groupService;

    public GroupDeleteCommand(GroupService groupService) {
        super(new CommandMeta("delete", "Deletes an existing multi-chat group (and unlinks all members).",
                List.of(new OptionMeta("name", "The name of the group.", OptionMeta.Type.STRING, true))));
        this.groupService = groupService;
    }

    @Override
    public void execute(CommandContext context) {
        String name = (String) context.getOption("name");
        Optional<MultichatGroup> groupOptional = groupService.getGroupByNameAndOwnerId(name, context.getAuthorId());
        if (groupOptional.isEmpty()) {
            context.send("Such a group doesn't exist! Did you type the name right? Try `/multichat list`.", true);
            return;
        }
        MultichatGroup group = groupOptional.get();
        groupService.deleteGroup(group);
        context.send("Group named `" + name + "` deleted.", true);
    }

}
