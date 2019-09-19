package com.zos.activiti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
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
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zos.activiti.beans.MyServiceTaskBean;
import com.zos.activiti.delegate.MyServiceTaskJavaDelegate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTaskTest {

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
		// JavaDelegate, ActivitiBehavior
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/serviceTaskDelegate.bpmn");
		Deployment deployment = deploymentBuilder.deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("myServiceTaskJavaDelegate", new MyServiceTaskJavaDelegate());
		vars.put("myServiceTaskBean", new MyServiceTaskBean());
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), vars);
		log.info("ProcessInstance {}", processInstance.getId());
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Task {}", taskService.getVariable(task.getId(), "myServiceTaskBean"));
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).onlyChildExecutions().singleResult();
		log.info("Execution {}", runtimeService.getVariable(execution.getId(), "myServiceTaskBean"));
		log.info("ProcessInstance {}", runtimeService.getVariable(processInstance.getId(), "myServiceTaskBean"));
		List<String> args = new ArrayList<>();
		args.add("cmd");
		args.add("/c");
		args.add("echo");
		args.add("hello");
		args.add("joker");
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		try {
			Process process = processBuilder.start();
			String result = convertStreamToString(process.getInputStream());
			log.info("Result {}", result);
		} catch (IOException e) {
			log.error("Error {}", e.getMessage());
		}
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		log.info("Task {}", taskService.getVariable(task.getId(), "javaHome"));
		execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).onlyChildExecutions().singleResult();
		log.info("Execution {}", runtimeService.getVariable(execution.getId(), "javaHome"));
		log.info("ProcessInstance {}", runtimeService.getVariable(processInstance.getId(), "javaHome"));
	}
	
	private String convertStreamToString(InputStream inputStream) throws IOException {
		if (inputStream != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			try {
				int length = 0;
				while ((length = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, length);
				}
			} finally {
				inputStream.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

}
