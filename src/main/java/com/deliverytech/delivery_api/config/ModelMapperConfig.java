package com.deliverytech.delivery_api.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configurações opcionais (recomendado)
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)  // Habilita matching por campo
                .setFieldAccessLevel(AccessLevel.PRIVATE)  // Acessa campos privados
                .setMatchingStrategy(MatchingStrategies.STRICT);  // Estratégia estrita

        return modelMapper;
    }
}