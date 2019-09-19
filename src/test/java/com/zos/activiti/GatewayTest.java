/**
 * 
 */
package com.zos.activiti;

import java.util.concurrent.TimeUnit;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
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
public class GatewayTest {

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
	public void contextLoads() throws InterruptedException {
		// 单向网关 1 有一条满足则执行该条;, 2 如果没有任何一条符合则执行默认, 若果连默认也没有则抛出错误信息;, 3 如果满足多条则选择流程图中配置的第一个执行;
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/sequence.bpmn");
		Deployment deployment = deploymentBuilder.deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Task {}", task.getName());
		taskService.setVariable(task.getId(), "days", 4);
		taskService.complete(task.getId());
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Task {}", task.getName());
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Task {}", task.getName());
		taskService.setVariable(task.getId(), "days", 6);
		taskService.complete(task.getId());
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Task {}", task.getName());
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Task {}", task.getName());
		taskService.setVariable(task.getId(), "days", 2);
		taskService.complete(task.getId());
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Task {}", task.getName());
		// 并行网关 在同一个节点上, 既可以汇合又可以分叉, 并不要求成对出现
		// 兼容网关 是一个并行和单向的结合体, 并不要求成对出现
		// 事件网关 只会朝事件所触发的那条分支走
		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/eventGateway.bpmn");
		deployment = deploymentBuilder.deploy();
		processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		runtimeService.signalEventReceived("eventGatewaySignal");
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Task {}", task.getName());
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		TimeUnit.SECONDS.sleep(10L);
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		if (task != null) {
			log.info("Task {}", task.getName());
		}
	}
}
