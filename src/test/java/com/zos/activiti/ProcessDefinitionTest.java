package com.zos.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessDefinitionTest {

	@Autowired
	ProcessEngine processEngine;

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	IdentityService identityService;

	@Test
	public void contextLoads() {
		String uId = UUID.randomUUID().toString();
		User user = identityService.newUser(uId);
		user.setFirstName("Joker");
		identityService.saveUser(user);
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/vacation.bpmn");
		deploymentBuilder.addClasspathResource("processes/vacation.png");
		Deployment deployment = deploymentBuilder.deploy();
		/**
		 * 流程定义, 在流程定义上配置用户权限, 而不是在流程部署上配置. 将用户信息或用户组信息和流程关联关系保存到流程运行时表中
		 * act_ru_identitylink
		 */
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		repositoryService.addCandidateStarterUser(processDefinition.getId(), user.getId());
		repositoryService.suspendProcessDefinitionById(processDefinition.getId());
		processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId())
				.singleResult();
		if (processDefinition.isSuspended()) {
			repositoryService.activateProcessDefinitionById(processDefinition.getId());
		}
		// 依据流程定义的 Key, 来启动流程.
		runtimeService.startProcessInstanceByKey(processDefinition.getKey());
		List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
				.startableByUser(uId).list();
		for (ProcessDefinition pd : processDefinitions) {
			log.info("ProcessDefinition Key : {}", pd.getKey());
		}
	}
}
