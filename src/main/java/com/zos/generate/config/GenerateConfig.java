package com.zos.generate.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zos.generate.generate.properties.GenerateProperties;
import com.zos.generate.generate.util.SpringHelper;

@Configuration
@EnableConfigurationProperties(GenerateProperties.class)
public class GenerateConfig {

	@Bean
	public SpringHelper springHelp(ApplicationContext applicationContext) {
		return new SpringHelper();
	}
	
	@Bean
	public GenerateProperties generateProperties(GenerateProperties generateProperties) {
		return generateProperties;
	}
	
}
