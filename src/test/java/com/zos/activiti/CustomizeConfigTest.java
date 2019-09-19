package com.zos.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomizeConfigTest {

	@Test
	public void contextLoads() {
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		log.info(processEngine.getName());
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		// 一次部署可以部署多个流程文件
		deploymentBuilder = deploymentBuilder.addClasspathResource("processes/vacation.bpmn");
		deploymentBuilder.name("Test");
		Deployment deployment = deploymentBuilder.deploy();
		log.info(deployment.getName());
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacation");
		// 普通员工完成请假的任务
		TaskService taskService = processEngine.getTaskService();
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("当前流程节点: {}", task.getName());
		taskService.complete(task.getId());
		// 经理审核任务
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("当前流程节点: {}", task.getName());
		taskService.complete(task.getId());
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("流程结束: {}", task);
		processEngine.close();
		// 自定义 Bean Name
		processEngineConfiguration = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		processEngine = processEngineConfiguration.buildProcessEngine();
		log.info(processEngine.getName());
		repositoryService = processEngine.getRepositoryService();
		deploymentBuilder = repositoryService.createDeployment();
		// 一次部署可以部署多个流程文件
		deploymentBuilder = deploymentBuilder.addClasspathResource("processes/vacation.bpmn");
		deploymentBuilder.name("Test");
		deployment = deploymentBuilder.deploy();
		log.info(deployment.getName());
		runtimeService = processEngine.getRuntimeService();
		processInstance = runtimeService.startProcessInstanceByKey("vacation");
		// 普通员工完成请假的任务
		taskService = processEngine.getTaskService();
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("当前流程节点: {}", task.getName());
		taskService.complete(task.getId());
		// 经理审核任务
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("当前流程节点: {}", task.getName());
		taskService.complete(task.getId());
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("流程结束: {}", task);
		processEngine.close();
	}
}
