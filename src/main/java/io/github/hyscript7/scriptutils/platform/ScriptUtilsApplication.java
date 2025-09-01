package io.github.hyscript7.scriptutils.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import io.github.hyscript7.scriptutils.config.ScriptUtilsConfig;

@SpringBootApplication(scanBasePackages = "io.github.hyscript7.scriptutils")
@EnableConfigurationProperties(ScriptUtilsConfig.class)
public class ScriptUtilsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScriptUtilsApplication.class, args);
	}

}
