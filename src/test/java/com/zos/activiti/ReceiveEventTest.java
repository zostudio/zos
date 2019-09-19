package com.zos.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReceiveEventTest {

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
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/receive.bpmn");
		Deployment deployment = deploymentBuilder.deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		// 查询当前的子执行流
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId())
				.onlyChildExecutions().singleResult();
		log.info("子执行流 {}", execution.getActivityId());

		// 需要触发对应的方法才可以继续往下走
		runtimeService.trigger(execution.getId());
		execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId())
				.onlyChildExecutions().singleResult();
		log.info("子执行流 {}", execution.getActivityId());

		// 捕获事件(Catching), 会一直等待事件的发生
		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/signal.bpmn");
		deployment = deploymentBuilder.deploy();
		processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId())
				.singleResult();
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId())
				.onlyChildExecutions().singleResult();
		log.info("子执行流 {}", execution.getActivityId());
		runtimeService.signalEventReceived("testSignal");
		execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId())
				.onlyChildExecutions().singleResult();
		log.info("子执行流 {}", execution.getActivityId());

		// 抛出事件(Throwing), 不会等待直接抛出事件

		// 消息中间事件(不在开始也不在结束, 只是在流程中间, 刚好又是消息事件, 因此称为消息中间事件, 也有两种消息, 捕获和抛出)
		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/message.bpmn");
		deployment = deploymentBuilder.deploy();
		processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId())
				.singleResult();
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId())
				.onlyChildExecutions().singleResult();
		log.info("子执行流 {}", execution.getActivityId());
		runtimeService.messageEventReceived("testMsg", execution.getId());
		execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId())
				.onlyChildExecutions().singleResult();
		log.info("子执行流 {}", execution.getActivityId());
	}
}
