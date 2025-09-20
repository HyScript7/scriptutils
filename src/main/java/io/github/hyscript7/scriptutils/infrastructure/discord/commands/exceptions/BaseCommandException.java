package io.github.hyscript7.scriptutils.infrastructure.discord.commands.exceptions;

/**
 * Base exception for all command-related exceptions
 */
public class BaseCommandException extends RuntimeException {

    public BaseCommandException(String message) {
        super(message);
    }
    
}
