package io.github.hyscript7.scriptutils.modules.developer.internal.commands.roles.management;

import io.github.hyscript7.scriptutils.domain.discord.commands.*;
import io.github.hyscript7.scriptutils.modules.developer.internal.ColorUtil;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.managers.RoleManager;
import org.jetbrains.annotations.Nullable;

public class EditRoleSubcommand extends Subcommand {
    public EditRoleSubcommand() {
        super(CommandMeta.builder().name("edit").description("Edits an existing role")
                .addOption("role", "The role to edit", OptionMeta.Type.ROLE, true)
                .addOption("name", "The new name for the role", OptionMeta.Type.STRING, false)
                .addOption("color", "The new color for the role", OptionMeta.Type.STRING, false)
                .addOption("distinct", "Whether the role should be shown separately in the member list", OptionMeta.Type.BOOLEAN, false, null)
                .addOption("mentionable", "Whether the role should be mentionable by anyone", OptionMeta.Type.BOOLEAN, false, null)
                .build());
    }

    @Override
    public void execute(CommandContext context) {
        Role role = (Role) context.getOption("role");
        @Nullable String newName = (String) context.getOption("name");
        @Nullable String colorString = (String) context.getOption("color");
        @Nullable Boolean distinct = (Boolean) context.getOption("distinct");
        @Nullable Boolean mentionable = (Boolean) context.getOption("mentionable");

        if (context.getGuild().isEmpty()) {
            context.send("This command can only be ran in a guild!", true);
            return;
        }

        if (newName == null && colorString == null) {
            context.send("You must specify at least one property to edit (name or color)!", true);
            return;
        }

        context.defer(true);

        boolean colorInvalid = false;
        StringBuilder response = new StringBuilder("Role <@&" + role.getIdLong() + "> updated!");

        RoleManager manager = role.getManager();

        if (newName != null) {
            manager = manager.setName(newName);
            response.append("\n- Name changed to: `").append(newName).append("`");
        }

        if (colorString != null) {
            Integer color = ColorUtil.parseColor(colorString);
            if (color == null) {
                color = 0;
                colorInvalid = true;
            }

            int finalColor = color;
            manager = manager.setColor(finalColor);

            if (colorInvalid) {
                response.append("\n**The provided color couldn't be recognized, defaulted to `no color`**");
            } else {
                response.append("\n- Color changed");
            }
        }

        if (distinct != null) {
            manager = manager.setMentionable(distinct);
            response.append("\n- Distinct (Hoisted): `").append(distinct ? "YES" : "NO").append("`");
        }

        if (mentionable != null) {
            manager = manager.setMentionable(mentionable);
            response.append("\n- Mentionable: `").append(mentionable ? "YES" : "NO").append("`");
        }

        manager.queue();

        context.send(response.toString());
    }
}