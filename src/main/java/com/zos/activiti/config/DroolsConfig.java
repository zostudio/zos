package com.zos.activiti.config;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class DroolsConfig {

	private KnowledgeBuilder knowledgeBuilder;
	
	@Bean
	@Order(value = 16)
	public KnowledgeBuilder knowledgeBuilders() {
		if (null == this.knowledgeBuilder) {
			this.knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		}
		return this.knowledgeBuilder;
	}
}
