package io.github.hyscript7.scriptutils.infrastructure;

import java.time.Duration;

import lombok.Getter;

public class Constants {
    public static final String DEFAULT_REASON = "No reason specified.";
    public static final Duration DEFAULT_PURGE_DURATION = Duration.ofDays(7L);

    @Getter
    public enum Colors {
        RED(0xFF0000),
        GREEN (0x00FF00),
        YELLOW(0xFFFF00);

        public final int value;
        Colors(int value) {
            this.value = value;
        }
    }

    private Constants() {}
}
