package io.github.hyscript7.scriptutils.domain.discord.commands;

public abstract class Subcommand extends CommandNode implements ExecutableCommand {

    protected Subcommand(CommandMeta meta) {
        super(meta);
    }

    @Override
    public abstract void execute(CommandContext context);
}
