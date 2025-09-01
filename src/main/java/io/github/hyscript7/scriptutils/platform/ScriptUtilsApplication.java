package io.github.hyscript7.scriptutils.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.github.hyscript7.scriptutils")
public class ScriptUtilsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScriptUtilsApplication.class, args);
	}

}
