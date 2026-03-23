package io.github.hyscript7.scriptutils.modules.developer.internal.commands.roles.management;

import io.github.hyscript7.scriptutils.domain.discord.commands.*;
import io.github.hyscript7.scriptutils.modules.developer.internal.ColorUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.Nullable;

public class CreateRoleSubcommand extends Subcommand {
    public CreateRoleSubcommand() {
        super(CommandMeta.builder().name("create").description("Creates a new role")
                .addOption("name", "The name of the role", OptionMeta.Type.STRING, true)
                .addOption("color", "The color of the role", OptionMeta.Type.STRING, false, 0)
                .addOption("mentionable", "Whether the role should be mentionable by anyone", OptionMeta.Type.BOOLEAN, false, false)
                .addOption("distinct", "Whether the role should be \"hoisted\" (shown separately in the member list)", OptionMeta.Type.BOOLEAN, false, false)
                .addOption("above", "The role to position this role above", OptionMeta.Type.ROLE, false, null)
                .build());
    }

    @Override
    public void execute(CommandContext context) {
        String name = (String) context.getOption("name");
        String colorString = (String) context.getOption("color");
        boolean mentionable = (boolean) context.getOption("mentionable");
        boolean distinct = (boolean) context.getOption("distinct");
        @Nullable Role above = (Role) context.getOption("above");

        GuildContext guild;
        if (context.getGuild().isEmpty()) {
            context.send("This command can only be ran in a guild!", true);
            return;
        }
        guild = context.getGuild().get();

        if(!guild.memberHasPermission(context.getAuthorId(),
                Permission.MANAGE_ROLES.getRawValue())) {
            context.send("You do not have permission to use this command!", true);
            return;
        }

        Integer color = ColorUtil.parseColor(colorString);

        boolean colorInvalid;

        if (color == null) {
            color = 0;
            colorInvalid = true;
        } else {
            colorInvalid = false;
        }

        context.defer(true);
        RoleContext newRole = guild.createRole(name, color, mentionable, distinct, above != null ? above.getIdLong() : null);
        context.send("Role <@&" + newRole.getRoleId() + "> created!" + (colorInvalid ? "\n**The provided color couldn't be recognized, defaulted to `no color`**" : ""));
    }

}
