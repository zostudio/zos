package com.zos.activiti;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.deploy.DeploymentCache;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zos.activiti.drools.Member;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class DroolsActivitiTest {
	
	@Autowired
	private KnowledgeBuilder knowledgeBuilder;

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
		// 添加规则到 KnowledgeBuilder
		knowledgeBuilder.add(ResourceFactory.newClassPathResource("drools/sale.drl", DroolsActivitiTest.class),
				ResourceType.DRL);
		// 获取知识规则包
		Collection<KnowledgePackage> kpackages = knowledgeBuilder.getKnowledgePackages();
		// 创建 KnowledgeBase 实例
		KnowledgeBase kbase = knowledgeBuilder.newKnowledgeBase();
		// 将知识规则包添加到 KnowledgeBase 中
		kbase.addKnowledgePackages(kpackages);
		// 使用 KnowledgeBase 实例, 创建 StatefulKnowledgeSession 实例
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		KnowledgeRuntimeLogger knowledgeRuntimeLogger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession,
				"log/drools");
		// 定义一个事实对象
		Member memeber = new Member();
		memeber.setIdentity("gold");
		memeber.setAmount(100);
		// 向 StatefulKnowledgeSession 中加入事实
		ksession.insert(memeber);
		// 规则匹配
		ksession.fireAllRules();
		log.info("Member {}", memeber.getResult());
		// 关闭 StatefulKnowledgeSession 的资源
		knowledgeRuntimeLogger.close();
		ksession.dispose();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/sale.bpmn");
		Deployment deployment = deploymentBuilder.deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).processDefinitionKey("saleProcess").singleResult();
		
		DeploymentManager deploymentManager = ((ProcessEngineConfigurationImpl)processEngine.getProcessEngineConfiguration()).getDeploymentManager();
		DeploymentCache<Object> knowledgeBaseCache = deploymentManager.getKnowledgeBaseCache();
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());

		// 添加规则到 KnowledgeBuilder
		knowledgeBuilder.add(org.drools.io.ResourceFactory.newClassPathResource("drools/sale.drl", DroolsActivitiTest.class),
				org.drools.builder.ResourceType.DRL);
		// 获取知识规则包
		Collection<org.drools.definition.KnowledgePackage> kps = knowledgeBuilder.getKnowledgePackages();
		
		org.drools.KnowledgeBase kba = knowledgeBuilder.newKnowledgeBase();
		kba.addKnowledgePackages(kps);
		knowledgeBaseCache.add(deployment.getId(), kba);
		log.info("ProcessInstance {}", processInstance.getId());
		// 完成第一个任务, 并设置销售参数
		Map<String, Object> vars = new HashMap<String, Object>();
		Member member = new Member();
		member.setIdentity("gold");
		member.setAmount(100);
		vars.put("sale", member);
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		taskService.complete(task.getId(), vars);
		List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).list();
		for (Execution execution : executions) {
			Object object = runtimeService.getVariable(execution.getId(), "result");
			log.info("Execution {}", execution.getActivityId());
			log.info("Object {}", object);
		}
	}

}
