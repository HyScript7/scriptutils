package io.github.hyscript7.scriptutils.modules.multichat.internal.commands;

import java.util.List;
import io.github.hyscript7.scriptutils.modules.multichat.internal.services.GroupService;
import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;

@Component
public class GroupCreateCommand extends Subcommand {

    private final GroupService groupService;

    public GroupCreateCommand(GroupService groupService) {
        super(new CommandMeta("create", "Creates a new multi-chat group.",
                List.of(new OptionMeta("name", "The name of the group.", OptionMeta.Type.STRING, true),
                        new OptionMeta("description", "A brief explanation of what the group is supposed to link.",
                                OptionMeta.Type.STRING, false))));
        this.groupService = groupService;
    }

    @Override
    public void execute(CommandContext context) {
        String name = (String) context.getOption("name");
        String description;
        try {
            description = (String) context.getOption("description");
        } catch (IllegalArgumentException e) {
            description = "No description provided.";
        }
        groupService.createGroup(context.getAuthorId(), name, description);
        context.send("Group named `" + name + "` created. Add channels to it using `/multichat join <name> [channel]`",
                true);
    }

}
