package com.zos.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.identity.NativeGroupQuery;
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
public class QueryGroupTest {

	@Autowired
	ProcessEngine processEngine;

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	TaskService taskService;

	@Autowired
	IdentityService identityService;

	@Test
	public void contextLoads() {
		log.info(this.processEngine.getName());
		String groupId = UUID.randomUUID().toString();
		Group group = this.identityService.newGroup(groupId);
		group.setName("Group A");
		group.setType("TYPE A");
		this.identityService.saveGroup(group);
		GroupQuery groupQuery = this.identityService.createGroupQuery();
		group = groupQuery.groupId(groupId).singleResult();
		log.info("Group Name: {}", group.getName());
		for (int i = 0; i < 10; i++) {
			groupId = UUID.randomUUID().toString();
			group = this.identityService.newGroup(groupId);
			group.setName("Group " + groupId);
			group.setType("TYPE " + groupId);
			this.identityService.saveGroup(group);
			group = groupQuery.groupId(groupId).singleResult();
			log.info("Group Name: {}", group.getName());
		}
		// 注意此处必须重新创建 GroupQuery 对象, 因为前面的已经添加 groupId 参数, 会导致查询结果错误.
		groupQuery = this.identityService.createGroupQuery();
		List<Group> groups = groupQuery.list();
		for (Group g : groups) {
			log.info("Group Name: {}", g.getName());
		}
		groupQuery = this.identityService.createGroupQuery();
		log.info("size: {}", groupQuery.count());
		groupQuery = this.identityService.createGroupQuery();
		groups = groupQuery.listPage(1, 5);
		for (Group g : groups) {
			log.info("Group Name: {}", g.getName());
		}
		groupQuery = this.identityService.createGroupQuery();
		groups = groupQuery.orderByGroupId().desc().orderByGroupName().asc().list();
		for (Group g : groups) {
			log.info("Group Name: {}", g.getName());
		}
		NativeGroupQuery nativeGroupQuery = this.identityService.createNativeGroupQuery();
		groups = nativeGroupQuery.sql("SELECT * FROM ACT_ID_GROUP WHERE NAME_ = #{name}").parameter("name", "GROUP A")
				.list();
		for (Group g : groups) {
			log.info("Group Name: {}", g.getName());
		}
	}

}
