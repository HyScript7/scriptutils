package io.github.hyscript7.scriptutils.infrastructure.discord.startup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.hyscript7.scriptutils.infrastructure.discord.CommandRegistry;
import jakarta.annotation.Nonnull;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

/**
 * This event listener exists for the sole purpose of sending discord our slash commands if they're out of sync.
 * This should run exactly once per startup.
 */
@Component
@Slf4j
public class SlashCommandRegistrator extends ListenerAdapter {
    private CommandRegistry commandRegistry;

    public SlashCommandRegistrator(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        JDA jda = event.getJDA();
        Map<String, SlashCommandData> remoteCommands = getSyncedSlashCommands(jda);
        List<SlashCommandData> localCommands = commandRegistry.getSlashCommands();

        int outOfSyncCount = countOutOfSyncCommands(localCommands, remoteCommands);

        if (outOfSyncCount > 0 || localCommands.size() != remoteCommands.size()) {
            log.info(
                    "Remote has {} commands, local has {}. {} slash commands are out of sync. Updating Discord...",
                    remoteCommands.size(), localCommands.size(), outOfSyncCount);
            jda.updateCommands()
                    .addCommands(localCommands)
                    .queue();
        } else {
            log.info(
                    "Remote has {} commands, local has {}. All slash commands are in sync.",
                    remoteCommands.size(), localCommands.size());
        }
    }

    /**
     * Counts how many commands are out of sync
     * 
     * @param localCommands
     * @param remoteCommands
     * @return The number of commands out of sync (mismatched or missing)
     */
    private int countOutOfSyncCommands(List<SlashCommandData> localCommands,
            Map<String, SlashCommandData> remoteCommands) {
        // Optimize: Once there is at least 1 mismatch, we can stop checking and return.
        // The program doesn't actually care about the exact number, since an update
        // request overwrites all of the commands.
        Set<String> localNames = localCommands.stream()
                .map(SlashCommandData::getName)
                .collect(Collectors.toSet());

        // Local commands missing or mismatched with remote
        long localMismatches = localCommands.stream()
                .filter(cmd -> !remoteCommands.containsKey(cmd.getName())
                        || !commandsMatch(remoteCommands.get(cmd.getName()), cmd))
                .count();

        // Remote commands not present locally
        long remoteExtra = remoteCommands.keySet().stream()
                .filter(remoteName -> !localNames.contains(remoteName))
                .count();

        return (int) (localMismatches + remoteExtra);
    }

    /**
     * Checks if two slash commands (data) are the same
     * 
     * @param remote the remote command data
     * @param local  the local command data
     * @return true if the commands are identical
     */
    private boolean commandsMatch(SlashCommandData remote, SlashCommandData local) {
        return remote.toData().equals(local.toData());
    }

    /**
     * !API CALL
     * Retrieves all synced slash commands from Discord
     * 
     * @param jda The JDA instance
     * @return A mapping of root slash command names to slash command data
     */
    private Map<String, SlashCommandData> getSyncedSlashCommands(JDA jda) {
        Map<String, SlashCommandData> syncedCommands = new HashMap<>();
        jda.retrieveCommands().complete().stream().map(SlashCommandData::fromCommand)
                .forEach(s -> syncedCommands.put(s.getName(), s));
        return syncedCommands;
    }

}
