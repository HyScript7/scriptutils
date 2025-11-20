package io.github.hyscript7.scriptutils.modules.multichat.internal.commands;

import io.github.hyscript7.scriptutils.modules.multichat.internal.models.MultichatBinding;
import io.github.hyscript7.scriptutils.modules.multichat.internal.models.MultichatGroup;
import io.github.hyscript7.scriptutils.modules.multichat.internal.services.GroupService;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.CommandContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.GuildContext;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;

@Component
public class GroupListLinkedCommand extends Subcommand {

    private final GroupService groupService;

    public GroupListLinkedCommand(GroupService groupService) {
        super(new CommandMeta("linked", "Lists all groups which channels in this guild are linked to."));
        this.groupService = groupService;
    }

    @Override
    public void execute(CommandContext context) {
        Optional<GuildContext> guildContext = context.getGuild();
        if (guildContext.isEmpty()) {
            context.send("This command can only be used in a guild!", false);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder("Linked groups:\n");
        List<MultichatBinding> memberships = groupService.getSyncedChannelsByGuild(guildContext.get().getGuildId());
        if (memberships.isEmpty()) {
            stringBuilder.append("None");
            context.send(stringBuilder.toString(), true);
            return;
        }
        memberships.stream().forEach(binding -> {
            MultichatGroup group = binding.getGroup();
            stringBuilder.append("- <#").append(binding.getChannelId()).append("> <-> ")
                    .append(group.getName()).append(" - ").append(group.getDescription()).append("\n");
        });
        context.send(stringBuilder.toString().trim(), true);
    }

}
