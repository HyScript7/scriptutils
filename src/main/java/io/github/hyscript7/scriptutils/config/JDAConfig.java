package io.github.hyscript7.scriptutils.config;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.hyscript7.scriptutils.infrastructure.discord.CommandHandler;
import io.github.hyscript7.scriptutils.infrastructure.discord.CommandRegistry;
import io.github.hyscript7.scriptutils.infrastructure.discord.ModuleRegistrar;
import io.github.hyscript7.scriptutils.infrastructure.discord.startup.SlashCommandRegistrar;
import io.github.hyscript7.scriptutils.domain.discord.Module;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

@Slf4j
@Configuration
public class JDAConfig {

    private final SlashCommandRegistrar slashCommandRegistrar;

    private final CommandHandler commandHandler;

    private final CommandRegistry commandRegistry;

    private final ModuleRegistrar moduleRegistrar;
    private final ScriptUtilsConfig scriptUtilsConfiguration;
    private final Module[] modules;
    private JDA jda;

    public JDAConfig(ScriptUtilsConfig scriptUtilsConfiguration, Module[] modules, ModuleRegistrar moduleRegistrar,
            CommandRegistry commandRegistry, CommandHandler commandHandler, SlashCommandRegistrar slashCommandRegistrar) {
        this.modules = modules;
        this.scriptUtilsConfiguration = scriptUtilsConfiguration;
        this.moduleRegistrar = moduleRegistrar;
        this.commandRegistry = commandRegistry;
        this.commandHandler = commandHandler;
        this.slashCommandRegistrar = slashCommandRegistrar;
    }

    @Bean
    JDABuilder jdaBuilder() {
        JDABuilder jdaBuilder = JDABuilder
                .createDefault(scriptUtilsConfiguration.token())
                .setActivity(Activity.listening("to your demands."))
                .enableIntents(EnumSet.allOf(GatewayIntent.class));
        jdaBuilder.addEventListeners(commandHandler, slashCommandRegistrar);
        Arrays.stream(modules).forEach(m -> moduleRegistrar.register(jdaBuilder, commandRegistry, m));
        return jdaBuilder;
    }

    @Bean
    JDA jda(JDABuilder jdaBuilder) {
        this.jda = jdaBuilder.build();
        return this.jda;
    }

    @PreDestroy
    public void shutdown() throws InterruptedException {
        if (jda != null) {
            log.info("Shutting down JDA...");
            jda.shutdown();
            try {
                if (!jda.awaitShutdown(10, TimeUnit.SECONDS)) {
                    log.warn("JDA did not shut down gracefully within 10 seconds!");
                }
            } catch (InterruptedException e) {
                log.error("Thread interrupt received while shutting down JDA!", e);
                throw new InterruptedException(e.getMessage());
            }
        }
    }
}
