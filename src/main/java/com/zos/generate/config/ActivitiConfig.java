package com.zos.generate.config;

import javax.sql.DataSource;

import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.history.HistoryLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActivitiConfig {
	
	@Autowired
	private DataSource dataSource;

	/**	
	 * 流程引擎配置
	 * 
	 * @return ProcessEngineConfiguration
	 */
	@Bean
	public ProcessEngineConfiguration processEngineConfiguration() {
		StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration = new StandaloneProcessEngineConfiguration();
		// 数据源
		standaloneProcessEngineConfiguration.setDataSource(dataSource);
		// 数据库策略, false(默认), true(更新), create-drop(创建,关闭引擎时执行才执行drop), drop-create(先删除再创建)
		standaloneProcessEngineConfiguration.setDatabaseSchemaUpdate("true");
		// 历史数据, none(不存数据,效率最高), activity(实例,行为), audit(实例,行为,任务,属性)默认值, full
		standaloneProcessEngineConfiguration.setHistoryLevel(HistoryLevel.FULL);
		// 异步任务
		// standaloneProcessEngineConfiguration.setAsyncExecutor(asyncExecutor);
		// 邮件服务器
		// standaloneProcessEngineConfiguration.setMailServers(mailServers);
		// 事件日志
		standaloneProcessEngineConfiguration.setEnableDatabaseEventLogging(true);
		// 流程部署时是否自动生成流程图片
		standaloneProcessEngineConfiguration.setCreateDiagramOnDeploy(true);
		return standaloneProcessEngineConfiguration;
	}
	
	/**
	 * 流程引擎
	 * 
	 * @param processEngineConfiguration 流程引擎配置
	 * @return ProcessEngine
	 */
	@Bean
	public ProcessEngine processEngine(ProcessEngineConfiguration processEngineConfiguration) {
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		return processEngine;
	}
	
	/**
	 * 存储服务, 提供一系列管理流程定义和部署的 API
	 * 
	 * @param processEngine 流程引擎
	 * @return RepositoryService
	 */
	@Bean
	public RepositoryService repositoryService(ProcessEngine processEngine) {
		return processEngine.getRepositoryService();
	}
	
	/**
	 * 运行时服务, 在流程运行时对流程实例进行管理与控制
	 * 
	 * @param processEngine 流程引擎
	 * @return RuntimeService
	 */
	@Bean
	public RuntimeService runtimeService(ProcessEngine processEngine) {
		return processEngine.getRuntimeService();
	}
	
	/**
	 * 任务服务, 对流程任务进行管理, 例如: 任务提醒, 任务完成, 创建任务, 分派任务等
	 * 
	 * @param processEngine 流程引擎
	 * @return TaskService
	 */
	@Bean
	public TaskService taskService(ProcessEngine processEngine) {
		return processEngine.getTaskService();
	}
	
	/**
	 * 角色服务, 提供对流程角色数据进行管理的 API, 这些角色数据包括用户组, 用户以及它们之间的关系
	 * 
	 * @param processEngine 流程引擎
	 * @return IdentityService
	 */
	@Bean
	public IdentityService identityService(ProcessEngine processEngine) {
		return processEngine.getIdentityService();
	}
	
	/**
	 * 管理服务, 提供对流程引擎进行管理和维护的服务
	 * 
	 * @param processEngine 流程引擎
	 * @return ManagementService
	 */
	@Bean
	public ManagementService managementService(ProcessEngine processEngine) {
		return processEngine.getManagementService();
	}

	/**
	 * 历史服务, 对流程的历史数据进行操作, 包括查询, 删除这些历史数据
	 * 
	 * @param processEngine 流程引擎
	 * @return HistoryService
	 */
	@Bean
	public HistoryService historyService(ProcessEngine processEngine) {
		return processEngine.getHistoryService();
	}

	/**
	 * 使用该服务, 可以不需要重新部署流程模型, 就可以实现对流程模型的部分修改
	 * 
	 * @param processEngine 流程引擎
	 * @return DynamicBpmnService
	 */
	@Bean
	public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine) {
		return processEngine.getDynamicBpmnService();
	}
	
}
