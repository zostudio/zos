package com.zos.activiti;

import java.util.concurrent.TimeUnit;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JobTest {

	@Autowired
	ProcessEngine processEngine;

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	IdentityService identityService;

	@Autowired
	ManagementService managementService;

	@Test
	public void contextLoads() throws InterruptedException {
		// 异步任务 act_ru_job
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/jobgne.bpmn");
		Deployment deployment = deploymentBuilder.deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		TimeUnit.SECONDS.sleep(1L);
		// 定时任务 act_ru_timer_job
		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/timerInter.bpmn");
		deployment = deploymentBuilder.deploy();
		processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId())
				.singleResult();
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		TimeUnit.SECONDS.sleep(15L);
		// 暂停的工作 act_ru_suspended_job
		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/suspendInter.bpmn");
		deployment = deploymentBuilder.deploy();
		processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId())
				.singleResult();
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		TimeUnit.SECONDS.sleep(10L);
		runtimeService.suspendProcessInstanceById(processInstance.getId());
		// 此处会将中中断表中的任务, 重新放回到定时任务里面
		runtimeService.activateProcessInstanceById(processInstance.getId());
		TimeUnit.SECONDS.sleep(20L);
		// 无法执行的工作 act_ru_deadletter_job
		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/exception.bpmn");
		deployment = deploymentBuilder.deploy();
		processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId())
				.singleResult();
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		log.info("ProcessInstance {}", processInstance.getId());
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId())
				.onlyChildExecutions().singleResult();
		Job job = managementService.createJobQuery().executionId(execution.getId()).singleResult();
		managementService.setJobRetries(job.getId(), 1);
		// 即使在配置文件中没有配置执行器, 也会强制执行 executeJob
		// managementService.executeJob(job.getId());

		/**
		 * 将一般工作表中的数据, 转到不可执行的工作表中 将不可执行的工作表中的数据, 转到一般工作表中 将定时工作表中的数据, 转到一般工作表中
		 * 没有删除中断的工作接口, 是因为中断工作是工作流引擎产生的
		 */
		// managementService.moveJobToDeadLetterJob(job.getId());
		// managementService.moveDeadLetterJobToExecutableJob(job.getId(), 1);
		// managementService.moveTimerToExecutableJob(job.getId());
		// managementService.deleteJob(job.getId());
		// managementService.deleteDeadLetterJob(job.getId());
		// managementService.deleteTimerJob(job.getId());
	}
}
