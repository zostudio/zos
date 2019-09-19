/**
 * 
 */
package com.zos.activiti;

import org.activiti.engine.IdentityService;
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

import lombok.extern.slf4j.Slf4j;

/**
 * @author 01Studio
 *
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SubProcessTest {

	@Autowired
	ProcessEngine processEngine;

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	IdentityService identityService;

	@Autowired
	TaskService taskService;

	@Test
	public void contextLoads() {
		// 嵌入式子流程
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/subProcessError.bpmn");
		Deployment deployment = deploymentBuilder.deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Task {}", task.getName());
		// CallActiviti
		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/subProcessCommon.bpmn");
		deployment = deploymentBuilder.deploy();
		deploymentBuilder = repositoryService.createDeployment();
		ProcessDefinition processDefinitionCommon = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		deploymentBuilder.addClasspathResource("processes/subProcessVacation.bpmn");
		deployment = deploymentBuilder.deploy();
		processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Task {}", task.getName());
		taskService.setVariable(task.getId(), "days", "15");
		taskService.complete(task.getId());
		ProcessInstance processInstanceCommon = runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinitionCommon.getId()).singleResult();
		processInstance = runtimeService.createProcessInstanceQuery().superProcessInstanceId(processInstance.getId()).singleResult();
		task = taskService.createTaskQuery().processInstanceId(processInstanceCommon.getId()).singleResult();
		log.info("Common Task {}", task.getName());
		Object object = taskService.getVariable(task.getId(), "newDays");
		log.info("Common Task {}", object);
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Sub Common Task {}", task.getName());
		taskService.complete(task.getId());
		task = taskService.createTaskQuery().processInstanceId(processInstanceCommon.getId()).singleResult();
		log.info("Common Task {}", task.getName());
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Sub Common Task {}", task.getName());
		taskService.setVariable(task.getId(), "myDays", "16");
		taskService.complete(task.getId());
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinition.getId()).singleResult();
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Common Task {}", task.getName());
		object = taskService.getVariable(task.getId(), "resultDays");
		log.info("Task {}", object);
		// 事件子流程(已经在前面演示过了)
		// AdHotProcess ordering Sequential 顺序执行, Parallel 并行执行
		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/adHocProcess.bpmn");
		deployment = deploymentBuilder.deploy();
		processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).activityId("adHocProcessSubProcess").singleResult();
		runtimeService.executeActivityInAdhocSubProcess(execution.getId(), "UserTaskB");
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDefinitionKey("UserTaskB").singleResult();
		taskService.complete(task.getId());
		log.info("Execution count {}", runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).count());
		runtimeService.completeAdhocSubProcess(execution.getId());
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Task {}", task.getName());
	}
}
