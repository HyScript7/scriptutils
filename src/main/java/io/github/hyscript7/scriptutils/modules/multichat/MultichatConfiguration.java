package io.github.hyscript7.scriptutils.modules.multichat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultichatConfiguration {

    @Bean
    public MultichatModule multichatModule() {
        MultichatModule module = new MultichatModule();
        return module;
    }
    
}
