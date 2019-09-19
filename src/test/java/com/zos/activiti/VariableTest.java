package com.zos.activiti;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class VariableTest {

	@Autowired
	ProcessEngine processEngine;

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	TaskService taskService;

	@Test
	public void contextLoads() throws IOException {
		String tId = UUID.randomUUID().toString();
		Task task = taskService.newTask(tId);
		task.setName("测试参数任务");
		taskService.saveTask(task);

		taskService.setVariable(tId, "var1", "Hello");
		taskService.setVariable(tId, "var2", 1);
		taskService.setVariable(tId, "var3", 0.1F);
		taskService.setVariable(tId, "var4", 0.2);
		taskService.setVariable(tId, "var5", 2L);
		taskService.setVariable(tId, "var6", true);
		taskService.setVariable(tId, "var7", new Date());
		taskService.setVariable(tId, "var8", 'C');
		Person person = new Person();
		person.setId(0L);
		person.setName("用户变量");
		taskService.setVariable(tId, "var9", person);
		person = taskService.getVariable(tId, "var9", Person.class);
		log.info("Person {} {}", person.getId(), person.getName());

		// 参数的作用域, 全流程有效, 本任务有效.
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/vacation.bpmn");
		deploymentBuilder.addClasspathResource("processes/vacation.png");
		Deployment deployment = deploymentBuilder.deploy();

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		// 设置任务本地变量
		taskService.setVariableLocal(task.getId(), "局部变量", "varLocal");
		taskService.setVariable(task.getId(), "全局变量", "var");
		log.info("Task {}, 局部变量 {}", task.getName(), taskService.getVariableLocal(task.getId(), "局部变量"));
		log.info("Task {}, 全局变量 {}", task.getName(), taskService.getVariable(task.getId(), "全局变量"));
		taskService.complete(task.getId());
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Task {}, 局部变量 {}", task.getName(), taskService.getVariableLocal(task.getId(), "局部变量"));
		log.info("Task {}, 局部获取全局变量 {}", task.getName(), taskService.getVariableLocal(task.getId(), "全局变量"));
		log.info("Task {}, 全局变量 {}", task.getName(), taskService.getVariable(task.getId(), "全局变量"));
		log.info("Task {}, 全局获取局部变量 {}", task.getName(), taskService.getVariable(task.getId(), "局部变量"));
		log.info("Task {}, 全局变量 {}", task.getName(), taskService.getVariable(task.getId(), "personName", String.class));
		log.info("Task {}, 局部获取全局变量 {}", task.getName(),
				taskService.getVariableLocal(task.getId(), "personName", String.class));

		taskService.createAttachment("web url", task.getId(), processInstance.getId(), "163.com", "163 web site",
				"https://www.163.com");
		ClassPathResource classPathResource = new ClassPathResource("processes/vacation.bpmn");
		taskService.createAttachment("流程图", task.getId(), processInstance.getId(), "请假流程图", "程序员请假流程图",
				classPathResource.getInputStream());
		List<Attachment> attachments = taskService.getProcessInstanceAttachments(processInstance.getId());
		for (Attachment attachment : attachments) {
			log.info("Attachment {}", attachment.getName());
		}
		attachments = taskService.getTaskAttachments(task.getId());
		for (Attachment attachment : attachments) {
			log.info("Attachment {}", attachment.getName());
		}
		for (Attachment attachment : attachments) {
			if (attachment.getName().equals("163.com")) {
				log.info("Attachment {}", taskService.getAttachment(attachment.getId()).getUrl());
			} else if (attachment.getName().equals("请假流程图")) {
				log.info("Attachment {}",
						IOUtils.toString(taskService.getAttachmentContent(attachment.getId()), String.valueOf(StandardCharsets.UTF_8)));
			}
		}
	}
}

class Person implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 989960727767062224L;

	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
