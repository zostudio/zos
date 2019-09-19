package com.zos.activiti;

import java.util.List;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskServiceTest {

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

	@SuppressWarnings("unused")
	@Test
	public void contextLoads() {
		// 运行时数据表 act_ru_identitylink
		String uId1 = UUID.randomUUID().toString();
		User user1 = identityService.newUser(uId1);
		user1.setFirstName("测试候选人(Candidate)用户");
		identityService.saveUser(user1);
		String uId2 = UUID.randomUUID().toString();
		User user2 = identityService.newUser(uId2);
		user2.setFirstName("测试备选人(Candidate)用户");
		identityService.saveUser(user2);
		String gId = UUID.randomUUID().toString();
		Group group = identityService.newGroup(gId);
		group.setName("测试候选人(Candidate)用户组");
		identityService.saveGroup(group);
		String tId = UUID.randomUUID().toString();
		Task task = taskService.newTask(tId);
		task.setName("测试任务");
		taskService.saveTask(task);
		// 设置任务的候选用户
		taskService.addCandidateUser(tId, uId1);
		// 设置任务的候选用户组
		taskService.addCandidateGroup(tId, gId);
		// 查询用户组作为候选用户组的任务
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(gId).list();
		for (Task t : tasks) {
			log.info("Candidate Group Task Name : {}", task.getName());
		}
		// 查询用户作为候选用户的任务
		tasks = taskService.createTaskQuery().taskCandidateUser(uId1).list();
		for (Task t : tasks) {
			log.info("Candidate User Task Name : {}", task.getName());
		}
		// 设置任务的持有人(被任务声明为持有人的人, 一个任务只有一个持有人, 只可设置一次持有人, 如果设置多次将会报错)
		taskService.claim(tId, uId1);
		// 查询用户作为持有人的任务
		tasks = taskService.createTaskQuery().taskAssignee(uId1).list();
		for (Task t : tasks) {
			log.info("Assignee Task Name : {}", task.getName());
		}
		// 设置任务的代理人(应当去代理这个任务的人, 一个任务只有一个代理人)
		taskService.setOwner(tId, uId1);
		// 查询用户作为代理人的任务
		tasks = taskService.createTaskQuery().taskOwner(uId1).list();
		for (Task t : tasks) {
			log.info("Owner Task Name : {}", task.getName());
		}
		// 设置任务的代理人(应当去代理这个任务的人, 一个任务只有一个代理人)
		taskService.setOwner(tId, uId2);
		// 查询用户作为代理人的任务
		tasks = taskService.createTaskQuery().taskOwner(uId2).list();
		for (Task t : tasks) {
			log.info("Owner Task Name : {}", task.getName());
		}
	}
}
