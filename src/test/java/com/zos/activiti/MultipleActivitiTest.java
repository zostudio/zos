/**
 * 
 */
package com.zos.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class MultipleActivitiTest {

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
		/**
		 * nrOfInstances: 实例总数, 使用 loopCardinality 元素由该元素决定, 使用外部集合的话, 由集合大小决定.
		 * nrOfActiveInstances: 当前正在执行的实例数, 如果是按顺序执行的多实例活动, 那么该值总是1, 如果是同时进行的多实例活动, 则每执行完一个实例, 该值将会减少1.
		 * nrOfCompletedInstances: 已经完成的实例数.
		 * loopCounter: 当前循环的索引
		 */
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/multiple.bpmn");
		Deployment deployment = deploymentBuilder.deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		List<String> datas = new ArrayList<String>();
		datas.add("a");
		datas.add("b");
		datas.add("c");
		datas.add("d");
		datas.add("e");
		datas.add("f");
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("datas", datas);
		datas = new ArrayList<String>();
		datas.add("1");
		datas.add("2");
		vars.put("data2", datas);
		vars.put("data", "mydata");
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), vars);
		log.info("ProcessInstance {}", processInstance.getId());
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
		for (Task task : tasks) {
			log.info("Task {}", task.getName());
		}
	}
}
