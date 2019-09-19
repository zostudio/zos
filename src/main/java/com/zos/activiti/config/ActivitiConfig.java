package com.zos.activiti.config;

import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.form.api.FormRepositoryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class ActivitiConfig {

	private ProcessEngine processEngine;

	/**
	 * 流程引擎
	 * 
	 * @return
	 */
	@Bean
	@Order(value = 1)
	public ProcessEngine processEngine() {
		if (null == this.processEngine) {
			this.processEngine = ProcessEngines.getDefaultProcessEngine();
		}
		return this.processEngine;
	}

	/**
	 * 存储服务
	 * 
	 * @return
	 */
	@Bean
	@Order(value = 2)
	public RepositoryService repositoryService() {
		return this.processEngine.getRepositoryService();
	}

	/**
	 * 运行时服务
	 * 
	 * @return
	 */
	@Bean
	@Order(value = 3)
	public RuntimeService runtimeService() {
		return this.processEngine.getRuntimeService();
	}

	/**
	 * 任务服务
	 * 
	 * @return
	 */
	@Bean
	@Order(value = 4)
	public TaskService taskService() {
		return this.processEngine.getTaskService();
	}

	/**
	 * 历史服务
	 * 
	 * @return
	 */
	@Bean
	@Order(value = 5)
	public HistoryService historyService() {
		return this.processEngine.getHistoryService();
	}

	/**
	 * 身份服务
	 * 
	 * @return
	 */
	@Bean
	@Order(value = 6)
	public IdentityService identityService() {
		return this.processEngine.getIdentityService();
	}

	/**
	 * 动态 BPMN 服务
	 * 
	 * @return
	 */
	@Bean
	@Order(value = 7)
	public DynamicBpmnService dynamicBpmnService() {
		return this.processEngine.getDynamicBpmnService();
	}

	/**
	 * 表单服务
	 * 
	 * @return
	 */
	@Bean
	@Order(value = 8)
	public FormService formService() {
		return this.processEngine.getFormService();
	}

	@Bean
	@Order(value = 9)
	public ManagementService managementService() {
		return this.processEngine.getManagementService();
	}

	@Bean
	@Order(value = 10)
	public FormRepositoryService formRepositoryService() {
		return this.processEngine.getFormEngineRepositoryService();
	}

	@Bean
	@Order(value = 11)
	public ProcessEngineConfiguration processEngineConfiguration() {
		return this.processEngine.getProcessEngineConfiguration();
	}
}
