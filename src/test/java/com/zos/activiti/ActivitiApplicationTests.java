package com.zos.activiti;

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

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiApplicationTests {

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Test
    public void contextLoads() {
        DeploymentBuilder deploymentBuilder = this.repositoryService.createDeployment();
        // 一次部署可以部署多个流程文件
        deploymentBuilder = deploymentBuilder.addClasspathResource("processes/vacation.bpmn");
        deploymentBuilder.name("Test");
        Deployment deployment = deploymentBuilder.deploy();
        log.info(deployment.getName());
        ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        ProcessInstance processInstance = this.runtimeService.startProcessInstanceById(processDefinition.getId());
        // 普通员工完成请假的任务
        Task task = this.taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        log.info("当前流程节点: {}", task.getName());
        this.taskService.complete(task.getId());
        // 经理审核任务
        task = this.taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        log.info("当前流程节点: {}", task.getName());
        this.taskService.complete(task.getId());
        task = this.taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        log.info("流程结束: {}", task);
        this.processEngine.close();
    }

}
