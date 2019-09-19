package com.zos.activiti;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
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
public class EventTest {

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

    @Autowired
    ManagementService managementService;

    @Test
    public void contextLoads() throws InterruptedException{
        // 定时器开始事件
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("processes/timerStart.bpmn");
        Deployment deployment = deploymentBuilder.deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        log.info("ProcessInstance {}", processInstance.getId());
        TimeUnit.SECONDS.sleep(15L);
        // 消息开始事件
        deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("processes/messageStart.bpmn");
        deployment = deploymentBuilder.deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        // 注意是 MessageName(所有的流程图中这个值是唯一的) 不是 MessageId, act_ru_event_subscr
        processInstance = runtimeService.startProcessInstanceByMessage("startEvent");
        log.info("ProcessInstance {}", processInstance.getId());
        // 错误开始事件(只能嵌套在事件子流程里面)
        deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("processes/errorStart.bpmn");
        deployment = deploymentBuilder.deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        log.info("ProcessInstance {}", processInstance.getId());
        // 错误结束事件
        deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("processes/errorEnd.bpmn");
        deployment = deploymentBuilder.deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        log.info("ProcessInstance {}", processInstance.getId());
        // 取消结束事件
        deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("processes/cancelEvent.bpmn");
        deployment = deploymentBuilder.deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        log.info("Task {}", task.getName());
        taskService.complete(task.getId());
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        log.info("Task {}", task.getName());
        log.info("ProcessInstance {}", processInstance.getId());
        // 终止结束事件
        deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("processes/terminalEvent.bpmn");
        deployment = deploymentBuilder.deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        Long executionCount = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).count();
        log.info("Execution Count {}", executionCount);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task taskTemp : tasks) {
            log.info("Task {}", taskTemp.getName());
            if ("taskA".equals(taskTemp.getName())) {
                taskService.complete(taskTemp.getId());
            }
        }
        executionCount = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).count();
        log.info("Execution Count {}", executionCount);
        log.info("ProcessInstance {}", processInstance.getId());
        // 定时器边界事件
        deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("processes/boundaryTimer.bpmn");
        deployment = deploymentBuilder.deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        log.info("ProcessInstance {}", processInstance.getId());
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task taskTemp : tasks) {
            log.info("Task {}", taskTemp.getName());
        }
        TimeUnit.SECONDS.sleep(15L);
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task taskTemp : tasks) {
            log.info("Task {}", taskTemp.getName());
        }
        // 错误边界事件
        deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("processes/boundaryError.bpmn");
        deployment = deploymentBuilder.deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        log.info("ProcessInstance {}", processInstance.getId());
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task taskTemp : tasks) {
            log.info("Task {}", taskTemp.getName());
        }
        // 信号边界事件
        deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("processes/boundarySignal.bpmn");
        deployment = deploymentBuilder.deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        log.info("ProcessInstance {}", processInstance.getId());
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task taskTemp : tasks) {
            log.info("Task {}", taskTemp.getName());
            if ("UserTaskA".equals(taskTemp.getName())) {
            	taskService.complete(taskTemp.getId());
            }
        }
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task taskTemp : tasks) {
            log.info("Task {}", taskTemp.getName());
        }
        runtimeService.signalEventReceived("BoundarySignal2");
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task taskTemp : tasks) {
            log.info("Task {}", taskTemp.getName());
        }
        // 定时器中间事件
        deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("processes/intermediateTimer.bpmn");
        deployment = deploymentBuilder.deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        log.info("ProcessInstance {}", processInstance.getId());
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task taskTemp : tasks) {
            log.info("Task {}", taskTemp.getName());
            if ("UserTaskA".equals(taskTemp.getName())) {
            	taskService.complete(taskTemp.getId());
            }
        }
        TimeUnit.SECONDS.sleep(10L);
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task taskTemp : tasks) {
            log.info("Task {}", taskTemp.getName());
        }
        // 消息中间事件
        deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("processes/intermediateSignal.bpmn");
        deployment = deploymentBuilder.deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        log.info("ProcessInstance {}", processInstance.getId());
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task taskTemp : tasks) {
            log.info("Task {}", taskTemp.getName());
            if ("UserTaskA".equals(taskTemp.getName())) {
            	taskService.complete(taskTemp.getId());
            }
        }
        runtimeService.signalEventReceived("intermediateSignal2");
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task taskTemp : tasks) {
            log.info("Task {}", taskTemp.getName());
        }
        // 补偿中间事件(注意回退的顺序, 回退的次数, 回退时可以拿到本地变量)
        deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("processes/intermediateCompensation.bpmn");
        deployment = deploymentBuilder.deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        log.info("ProcessInstance {}", processInstance.getId());
    }
}
