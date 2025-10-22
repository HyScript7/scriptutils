package io.github.hyscript7.scriptutils.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.github.hyscript7.scriptutils.config.ScriptUtilsConfig;

@SpringBootApplication(scanBasePackages = "io.github.hyscript7.scriptutils")
@EnableJpaRepositories(basePackages = "io.github.hyscript7.scriptutils")
@EntityScan(basePackages = "io.github.hyscript7.scriptutils")
@EnableConfigurationProperties(ScriptUtilsConfig.class)
public class ScriptUtilsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScriptUtilsApplication.class, args);
	}

}
