package io.github.hyscript7.scriptutils.modules.multichat.internal.services;
import io.github.hyscript7.scriptutils.modules.multichat.internal.repositories.MultichatBindingRepository;
import io.github.hyscript7.scriptutils.modules.multichat.internal.repositories.MultichatGroupRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.hyscript7.scriptutils.modules.multichat.internal.models.MultichatBinding;
import io.github.hyscript7.scriptutils.modules.multichat.internal.models.MultichatGroup;

@Service
public class GroupService {

    private final MultichatBindingRepository multichatBindingRepository;

    private final MultichatGroupRepository multichatGroupRepository;

    GroupService(MultichatGroupRepository multichatGroupRepository, MultichatBindingRepository multichatBindingRepository) {
        this.multichatGroupRepository = multichatGroupRepository;
        this.multichatBindingRepository = multichatBindingRepository;
    }

    public MultichatGroup createGroup(long ownerId, String name, String description) {
        MultichatGroup group = MultichatGroup.builder()
                .ownerId(ownerId)
                .name(name)
                .description(description)
                .build();
        return multichatGroupRepository.save(group);
    }

    public MultichatGroup updateGroup(MultichatGroup group) {
        return multichatGroupRepository.save(group);
    }

    public void deleteGroup(MultichatGroup group) {
        multichatGroupRepository.delete(group);
    }

    public List<MultichatGroup> getGroupsBelongingToUser(long ownerId) {
        return multichatGroupRepository.findByOwnerId(ownerId);
    }

    public Optional<MultichatGroup> getGroupByNameAndOwnerId(String name, long ownerId) {
        return multichatGroupRepository.findByNameAndOwnerId(name, ownerId);
    }

    public MultichatBinding joinGroup(MultichatGroup group, long channelId, long guildId, String webhookUrl) {
        MultichatBinding multichatBinding = MultichatBinding.builder()
                .channelId(channelId)
                .guildId(guildId)
                .webhookUrl(webhookUrl)
                .group(group).build();
        return multichatBindingRepository.save(multichatBinding);
    }

    public void leaveGroup(MultichatBinding membership) {
        multichatBindingRepository.delete(membership);
    }

    public Optional<MultichatBinding> getChannelGroup(long channelId) {
        return multichatBindingRepository.findByChannelId(channelId);
    }

    public List<MultichatBinding> getSyncedChannelsByGuild(long guildId) {
        return multichatBindingRepository.findByGuildId(guildId);
    }

    public List<MultichatBinding> getNeighbors(MultichatBinding membership) {
        return membership.getGroup().getChannels();
    }

    public void unregisterViaWebhook(String webhookUrl) {
        multichatBindingRepository.findByWebhookUrl(webhookUrl).ifPresent(this::leaveGroup);
    }

}
