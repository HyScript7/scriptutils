package io.github.hyscript7.scriptutils.infrastructure.discord;

import org.springframework.stereotype.Component;

import io.github.hyscript7.scriptutils.domain.discord.Module;
import io.github.hyscript7.scriptutils.domain.discord.commands.Command;
import io.github.hyscript7.scriptutils.domain.discord.commands.CommandGroup;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.EventListener;

@Component
@Slf4j
public class ModuleRegistrar {

    /**
     * Registers a module with JDA.
     *
     * @param jdaBuilder      The JDA builder to register the module with
     * @param commandRegistry The command registry to register the module's commands
     *                        with
     * @param module          The module to register
     */
    public void register(JDABuilder jdaBuilder, CommandRegistry commandRegistry, Module module) {
        log.debug("Registering module {} with JDA.", module.getMeta().getName());
        module.getEventListeners().forEach(
                listener -> {
                    try {
                        if (listener instanceof EventListener) {
                            jdaBuilder.addEventListeners(listener);
                        } else {
                            throw new IllegalArgumentException(
                                    "Invalid event listener type: " + listener.getClass().getName()
                                            + ". Modules can only contain JDA event listeners.");
                        }
                        log.debug("Registered event listener {} of module {}!", listener.getClass().getName(),
                                module.getMeta().getName());
                    } catch (IllegalArgumentException e) {
                        log.error("Failed to register event listener {} of module {}! {}",
                                listener.getClass().getName(),
                                module.getMeta().getName(), e.getMessage());
                    }
                });
        log.info("Loaded {} event listeners from module {}.", module.getEventListeners().size(), module.getMeta().getName());
        module.getCommands().forEach(
                command -> {
                    try {
                        switch (command) {
                            case Command rootCommand -> commandRegistry.registerCommand(rootCommand);
                            case CommandGroup commandGroup -> commandRegistry.registerCommand(commandGroup);
                            default ->
                                throw new IllegalArgumentException("Invalid command type: "
                                        + command.getClass().getName()
                                        + ". Modules can only contain root commands (Command and CommandGroup).");
                        }
                        log.debug("Registered command {} of module {}!", command.getName(), module.getMeta().getName());
                    } catch (IllegalArgumentException e) {
                        log.error("Failed to register command {} of module {}! {}", command.getName(),
                                module.getMeta().getName(), e.getMessage());
                        throw e;
                    }
                });
        log.info("Loaded {} commands from module {}.", module.getCommands().size(), module.getMeta().getName());
    }

}
