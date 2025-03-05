package com.evo.common.webapp.config.jackson;

import java.time.LocalDate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
public class JacksonConfiguration {

    @Bean
    public SimpleModule localDateSerializer() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, new CustomLocalDateSerializer());
        return module;
    }
}
