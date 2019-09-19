package com.zos.activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
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

import com.zos.activiti.beans.MyUserTaskAllBean;
import com.zos.activiti.candidate.AuthService;
import com.zos.activiti.delegate.UserTaskAllJobDelegate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskTest {

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
		// 运行时数据表 act_ru_identitylink
		String uId = "joker";
		User user = identityService.newUser(uId);
		user.setFirstName("joker");
		identityService.saveUser(user);
		String gId1 = "management";
		Group group1 = identityService.newGroup(gId1);
		group1.setName("management");
		identityService.saveGroup(group1);
		String gId2 = "boss";
		Group group2 = identityService.newGroup(gId2);
		group2.setName("boss");
		identityService.saveGroup(group2);
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/userTaskCandidate.bpmn");
		Deployment deployment = deploymentBuilder.deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("authService", new AuthService());
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), vars);
		log.info("ProcessInstance {}", processInstance.getId());
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(gId1).list();
		for (Task task : tasks) {
			log.info("Task {}", task.getName());
		} 
		tasks = taskService.createTaskQuery().taskCandidateGroup(gId2).list();
		for (Task task : tasks) {
			log.info("Task {}", task.getName());
		}
		tasks = taskService.createTaskQuery().taskCandidateUser(uId).list();
		for (Task task : tasks) {
			log.info("Task {}", task.getName());
		}
		// 因为已经指定任务受托人, 所以上面的查询均为空, 如果不指定任务受托人, 则上面的查询均可以查询到数据
		tasks = taskService.createTaskQuery().taskAssignee(uId).list();
		for (Task task : tasks) {
			log.info("Task {}", task.getName());
		}
		tasks = taskService.createTaskQuery().taskAssignee(uId).list();
		for (Task task : tasks) {
			if ("UserTaskA".equals(task.getName())) {
				taskService.complete(task.getId());
			}
		}
		tasks = taskService.createTaskQuery().taskAssignee(uId).list();
		for (Task task : tasks) {
			log.info("Task {}", task.getName());
		}
		tasks = taskService.createTaskQuery().taskAssignee(uId).list();
		for (Task task : tasks) {
			if ("UserTaskB".equals(task.getName())) {
				taskService.complete(task.getId());
			}
		}
		tasks = taskService.createTaskQuery().taskCandidateUser(uId).list();
		for (Task task : tasks) {
			log.info("Task {}", task.getName());
		}
		tasks = taskService.createTaskQuery().taskCandidateUser(uId).list();
		for (Task task : tasks) {
			if ("UserTaskC".equals(task.getName())) {
				taskService.complete(task.getId());
			}
		}
		tasks = taskService.createTaskQuery().taskCandidateGroup(gId1).list();
		for (Task task : tasks) {
			log.info("Task {}", task.getName());
		}
		tasks = taskService.createTaskQuery().taskCandidateGroup(gId1).list();
		for (Task task : tasks) {
			if ("UserTaskD".equals(task.getName())) {
				taskService.complete(task.getId());
			}
		}
		tasks = taskService.createTaskQuery().taskAssignee(uId).list();
		for (Task task : tasks) {
			log.info("Task {}", task.getName());
		}
		
		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/userTaskListener.bpmn");
		deployment = deploymentBuilder.deploy();
		processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		vars = new HashMap<String, Object>();
		vars.put("myUserTaskAllBean", new MyUserTaskAllBean());
		vars.put("userTaskAllJobDelegate", new UserTaskAllJobDelegate());
		vars.put("userName", "joker");
		processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), vars);
		log.info("ProcessInstance {}", processInstance.getId());
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), "joker");
		taskService.complete(task.getId());
	}
}
