package com.zos.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessInstanceTest {

	@Autowired
	ProcessEngine processEngine;

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	TaskService taskService;

	@SuppressWarnings("unused")
	@Test
	public void contextLoads() throws IOException {
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/vacation.bpmn");
		deploymentBuilder.addClasspathResource("processes/vacation.png");
		Deployment deployment = deploymentBuilder.deploy();

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		// act_ru_execution, 一条主执行流, 一条子执行流
		log.info("ProcessInstance {}", processInstance.getId());
		// 主执行流和子执行流, 一条主执行流, 两条子执行流
		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/multi.bpmn");
		deployment = deploymentBuilder.deploy();
		processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId())
				.singleResult();
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());

		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/vacation.bpmn");
		deployment = deploymentBuilder.deploy();
		processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId())
				.singleResult();
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		// 在主执行流所在行设置 BUSINESS_KEY_, 方便后面用来查询
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), "BUSINESS_KEY_");
		log.info("ProcessInstance {}", processInstance.getId());
		processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());
		log.info("ProcessInstance {}", processInstance.getId());
		// 在主执行流所在行设置 BUSINESS_KEY_, 方便后面用来查询
		processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey(), "BUSINESS_KEY_");
		log.info("ProcessInstance {}", processInstance.getId());

		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/multi.bpmn");
		deployment = deploymentBuilder.deploy();
		processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId())
				.singleResult();
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
		// 通过 RuntimeService 设置的变量和在流程图中设置的变量一样
		for (Task task : taskList) {
			Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId())
					.singleResult();
			if (task.getName().equals("TaskA")) {
				runtimeService.setVariableLocal(execution.getId(), "TaskA", "varLocal");
			}
			if (task.getName().equals("TaskB")) {
				runtimeService.setVariable(execution.getId(), "TaskB", "var");
			}
			taskService.complete(task.getId());
		}
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("TaskA Local {}", runtimeService.getVariableLocal(processInstance.getId(), "TaskA"));
		log.info("TaskA {}", runtimeService.getVariable(processInstance.getId(), "TaskA"));
		log.info("TaskB Local {}", runtimeService.getVariableLocal(processInstance.getId(), "TaskB"));
		log.info("TaskB {}", runtimeService.getVariable(processInstance.getId(), "TaskB"));
	}
}
