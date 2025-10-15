package io.github.hyscript7.scriptutils.infrastructure.discord;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.commands.Command;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandGroup;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.OptionMeta;
import io.github.hyscript7.scriptutils.domain.discord.commands.Subcommand;
import io.github.hyscript7.scriptutils.domain.discord.commands.SubcommandGroup;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

/**
 * Utility class which allows us to convert a domain command tree into to a JDA
 * command tree.
 * 
 * It is a nightmare and I wouldn't wish it upon my worst enemy to have to
 * anyhow modify this.
 */
@Component
public class JDACommandTreeAdapter {
    /**
     * Converts a domain command tree into a JDA command tree (slash command)
     * 
     * @param root The domain command tree
     * @return The JDA command tree
     */
    public SlashCommandData toSlashCommandData(CommandGroup root) {
        CommandMeta meta = root.getMeta();
        SlashCommandData data = Commands.slash(meta.getName(), meta.getDescription());
        addOptions(data, meta);

        // Behold: for loop from hell
        root.getChildren().values().forEach(child -> {
            // The tree doesn't distinguish Subcommands from SubcommandGroups for
            // CommandGroups, so we have to do it here
            if (child instanceof Subcommand) {
                Subcommand sub = (Subcommand) child;
                data.addSubcommands(toSubcommandData(sub));
            } else if (child instanceof SubcommandGroup) {
                SubcommandGroup group = (SubcommandGroup) child;
                SubcommandGroupData groupData = new SubcommandGroupData(group.getName(), group.getDescription());

                group.getSubcommands().values().forEach(sub -> groupData.addSubcommands(toSubcommandData(sub)));

                data.addSubcommandGroups(groupData);
            } else {
                // And because of how abstraction works, someone can just pass complete bs in
                // here
                throw new IllegalArgumentException("Invalid child node type: " + child.getClass());
            }
        });

        return data;
    }

    /**
     * Converts a domain top level command into a JDA slash command (data)
     * 
     * @param command The domain command
     * @return The JDA slash command
     */
    public SlashCommandData toSlashCommandData(Command command) {
        CommandMeta meta = command.getMeta();
        SlashCommandData data = Commands.slash(meta.getName(), meta.getDescription());
        addOptions(data, meta);
        return data;
    }

    /**
     * Converts a domain subcommand to a JDA subcommand
     * 
     * @param sub The domain subcommand
     * @return The JDA subcommand data
     */
    private SubcommandData toSubcommandData(Subcommand sub) {
        SubcommandData data = new SubcommandData(sub.getName(), sub.getDescription());
        addOptions(data, sub.getMeta());
        return data;
    }

    /**
     * Adds domain options to a command's data (converts them to JDA options behind
     * the scenes)
     * 
     * @param data An instance of SlashCommandData
     * @param meta The full meta object containing the domain options
     */
    private void addOptions(SlashCommandData data, CommandMeta meta) {
        for (OptionMeta opt : meta.getOptions()) {
            data.addOptions(toOptionData(opt));
        }
    }

    /**
     * Adds domain options to a subcommand's data (converts them to JDA options
     * behind the scenes)
     * 
     * @param data An instance of SubcommandData
     * @param meta The full meta object containing the domain options
     */
    private void addOptions(SubcommandData data, CommandMeta meta) {
        for (OptionMeta opt : meta.getOptions()) {
            data.addOptions(toOptionData(opt));
        }
    }

    /**
     * Converts a domain option to a JDA option
     * 
     * @param opt The domain option
     * @return The JDA option
     */
    private OptionData toOptionData(OptionMeta opt) {
        OptionType jdaType = mapType(opt.getType());
        return new OptionData(jdaType, opt.getName(), opt.getDescription(), opt.isRequired());
    }

    /**
     * Converts a domain option type to a JDA option type
     * 
     * @param type The domain option type
     * @return The JDA option type
     */
    private OptionType mapType(OptionMeta.Type type) {
        switch (type) {
            case STRING:
                return OptionType.STRING;
            case INTEGER:
                return OptionType.INTEGER;
            case DOUBLE:
                return OptionType.NUMBER;
            case BOOLEAN:
                return OptionType.BOOLEAN;
            case USER:
                return OptionType.USER;
            case CHANNEL:
                return OptionType.CHANNEL;
            case ROLE:
                return OptionType.ROLE;
            default:
                throw new IllegalArgumentException("Unsupported option type: " + type
                        + ", did you forget to implement a resolver in the Tree adapter?");
        }
    }
}
