package io.github.hyscript7.scriptutils.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationProperties(prefix = "scriptutils")
@ConfigurationPropertiesScan
public record ScriptUtilsConfig(String token) {
}
