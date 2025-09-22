package io.github.hyscript7.scriptutils.domain.discord.commands;

public abstract class Command extends CommandNode implements ExecutableCommand {

    protected Command(CommandMeta meta) {
        super(meta);
    }

    @Override
    public abstract void execute(CommandContext context);
}
