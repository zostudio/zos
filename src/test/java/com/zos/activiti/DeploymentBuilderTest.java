package com.zos.activiti;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
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
public class DeploymentBuilderTest {

	@Autowired
	ProcessEngine processEngine;

	@Autowired
	RepositoryService repositoryService;

	@Test
	public void contextLoads() throws IOException {
		/**
		 * 测试 ZIP 压缩包部署
		 */
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		ClassPathResource classPathResource = new ClassPathResource("DeploymentBuilderTest.zip");
		ZipInputStream zipInputStream = new ZipInputStream(classPathResource.getInputStream());
		deploymentBuilder.addZipInputStream(zipInputStream);
		Deployment deployment = deploymentBuilder.deploy();

		/**
		 * 测试 txt 部署
		 */
		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("DeploymentBuilderTest.txt");
		deployment = deploymentBuilder.deploy();
		// 数据查询
		InputStream inputStream = repositoryService.getResourceAsStream(deployment.getId(),
				"DeploymentBuilderTest.txt");
		Integer available = inputStream.available();
		byte[] contents = new byte[available];
		inputStream.read(contents);
		String result = new String(contents);
		log.info("{}", result);

		/**
		 * 测试 BPMN Model
		 */
		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addBpmnModel("bpmnModel", createBpmnModel());
		// 不进行格式验证, 直接部署流程图
		deploymentBuilder.disableSchemaValidation();
		// 不进行 BPMN 验证, 直接部署流程图
		deploymentBuilder.disableBpmnValidation();
		deployment = deploymentBuilder.deploy();

		/**
		 * 测试通过流程定义查询流程部署文件
		 */
		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/vacation.bpmn");
		deployment = deploymentBuilder.deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		inputStream = repositoryService.getProcessModel(processDefinition.getId());
		available = inputStream.available();
		contents = new byte[available];
		inputStream.read(contents);
		result = new String(contents);
		log.info("{}", result);
		inputStream.close();

		/**
		 * 测试通过流程定义查询流程图片
		 */
		deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("processes/vacation.bpmn");
		deployment = deploymentBuilder.deploy();
		processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId())
				.singleResult();
		inputStream = repositoryService.getProcessDiagram(processDefinition.getId());
		// 将输入流转换为图片
		BufferedImage bufferedImage = ImageIO.read(inputStream);
		// 保存图片为文件
		File file = new File(new ClassPathResource("DeploymentBuilderTest.png").getPath());
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ImageIO.write(bufferedImage, "png", fileOutputStream);
		fileOutputStream.close();
		inputStream.close();

		/**
		 * 删除流程部署 1. 不管删除部署数据时是否指定级联删除, 部署的相关数据均会被删除, 包括身份数据 IdentityLink, 流程定义数据
		 * ProcessDefinition, 流程资源 Resource 与部署数据 Deployment 2. 如果设直为级联删除, 则会删除 流程实例数据
		 * ProcessInstance, 其中流程实例数据也包括 流程任务 Task, 与流程实例的历史数据 3. 如果设直为不进行级联删除的话, 如果
		 * Activiti 数据库中已经存在流程实例数据, 那么将会删除失败, 因为在删除流程定义时, 流程定义数据的主键已经被流程实例的相关数据所引用
		 */

	}

	private BpmnModel createBpmnModel() {
		// 创建 BPMN 模型对象
		BpmnModel bpmnModel = new BpmnModel();
		// 创建流程定义
		Process process = new Process();
		bpmnModel.addProcess(process);
		process.setId("bpmnModel");
		process.setName("bpmnModel");
		// 开始事件
		StartEvent startEvent = new StartEvent();
		startEvent.setId("startEvent");
		startEvent.setName("startEvent");
		process.addFlowElement(startEvent);
		// 用户任务
		UserTask userTask = new UserTask();
		userTask.setId("userTask");
		userTask.setName("userTask");
		process.addFlowElement(userTask);
		// 结束事件
		EndEvent endEvent = new EndEvent();
		endEvent.setId("endEvent");
		endEvent.setName("endEvent");
		process.addFlowElement(endEvent);
		// 流程顺序流
		process.addFlowElement(new SequenceFlow("startEvent", "userTask"));
		process.addFlowElement(new SequenceFlow("userTask", "endEvent"));
		return bpmnModel;
	}
}
