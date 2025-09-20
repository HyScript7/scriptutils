package io.github.hyscript7.scriptutils.infrastructure.discord.commands.exceptions;

/**
 * Thrown when discord requests a command which we do not have.
 */
public class CommandNotFoundException extends BaseCommandException {
    public CommandNotFoundException(String message) {
        super(message);
    }
}
