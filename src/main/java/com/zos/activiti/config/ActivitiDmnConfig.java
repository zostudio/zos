package com.zos.activiti.config;

import org.activiti.dmn.api.DmnRepositoryService;
import org.activiti.dmn.api.DmnRuleService;
import org.activiti.dmn.engine.DmnEngine;
import org.activiti.dmn.engine.DmnEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author 01Studio
 *
 */
@Configuration
public class ActivitiDmnConfig {
	
	private DmnEngineConfiguration dmnEngineConfiguration;
	
	private DmnEngine dmnEngine;
	
	/**
	 * 
	 * @return
	 */
	@Bean
	@Order(value = 12)
	public DmnEngineConfiguration dmnEngineConfiguration() {
		if (this.dmnEngineConfiguration == null) {
			this.dmnEngineConfiguration = DmnEngineConfiguration.createDmnEngineConfigurationFromResourceDefault();
		}
		return this.dmnEngineConfiguration;
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	@Order(value = 13)
	public DmnEngine dmnEngine() {
		if (this.dmnEngine == null) {
			this.dmnEngine = this.dmnEngineConfiguration.buildDmnEngine();
		}
		return this.dmnEngine;
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	@Order(value = 14)
	public DmnRepositoryService dmnRepositoryService() {
		return this.dmnEngine.getDmnRepositoryService();
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	@Order(value = 15)
	public DmnRuleService dmnRuleService() {
		return this.dmnEngine.getDmnRuleService();
	}
}
