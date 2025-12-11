package io.github.hyscript7.scriptutils.domain.discord.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public class DefaultOptionValue<T> {
    @Nullable T value;
}
