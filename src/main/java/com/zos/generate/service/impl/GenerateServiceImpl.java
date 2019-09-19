package com.zos.generate.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.impl.util.io.ResourceStreamSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zos.generate.bo.WorkflowBO;
import com.zos.generate.bo.WorkflowQueryPageBO;
import com.zos.generate.bo.WorkflowTemplateBO;
import com.zos.generate.bo.WorkflowTemplateQueryPageBO;
import com.zos.generate.common.ActivtiAnalysisUtil;
import com.zos.generate.common.Page;
import com.zos.generate.common.ProcessGenerateModel;
import com.zos.generate.generate.GeneratorFacade;
import com.zos.generate.generate.properties.GenerateProperties;
import com.zos.generate.generate.util.StringHelper;
import com.zos.generate.mapper.GenerateMapper;
import com.zos.generate.service.IGenerateService;
import com.zos.generate.service.IWorkflowService;
import com.zos.generate.service.IWorkflowTemplateService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Service("generateService")
@Transactional(rollbackFor = Exception.class)
public class GenerateServiceImpl implements IGenerateService {
	
	@Autowired
	private GeneratorFacade generatorFacade;
	
	@Autowired
	private GenerateProperties generateProperties;
	
	@Autowired
	private IWorkflowService workflowService;
	
	@Autowired
	private IWorkflowTemplateService workflowTemplateService;
	
	@Override
	public void userTask() {
		// TODO Auto-generated method stub

	}

	@Override
	public void signUserTask() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void endEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<WorkflowBO> workflow(String fileName, Long startId) throws Exception {
		List<WorkflowBO> result = null;
		ResourceStreamSource resourceStreamSource = new ResourceStreamSource("process/"+fileName+".bpmn");
		BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
		BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(resourceStreamSource, true, true);
		// 检测数据库中的数据是否重复
		if (this.checkExist(bpmnModel.getMainProcess().getId())) {
			return result;
		}
		// 分析流程图取得人工任务, 开始事件和结束事件
		List<FlowElement> userTasksAndEvents = ActivtiAnalysisUtil.getUserTaskAndEvent(bpmnModel.getMainProcess().getFlowElements());
		if (CollectionUtils.isEmpty(userTasksAndEvents)) {
			return null;
		}
		List<WorkflowBO> workflowBOs = GenerateMapper.INSTANCE.flowElementsToWorkflowBOs(userTasksAndEvents);
		for (WorkflowBO workflowBO : workflowBOs) {
			workflowBO.setWfItemId(startId);
			workflowBO.setLinkId(startId);
			workflowBO.setPhaseId("");
			workflowBO.setPhaseName("");
			workflowBO.setProcessKey(bpmnModel.getMainProcess().getId());
			workflowBO.setBackBtn("N");
			workflowBO.setCanBack("N");
			workflowBO.setLinkNoType(ActivtiAnalysisUtil.getLinkNoType(bpmnModel.getMainProcess().getFlowElementMap().get(workflowBO.getVmTaskNo())));
			workflowBO.setLinkNo(workflowBO.getVmTaskNo());
			startId++;
		}
		return workflowBOs;
	}

	@Override
	public String generateRest(String fileName, List<String> tableNames) throws Exception {
		ResourceStreamSource resourceStreamSource = new ResourceStreamSource("process/"+fileName+".bpmn");
		BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
		BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(resourceStreamSource, true, true);
		WorkflowQueryPageBO workflowQueryPageBO = new WorkflowQueryPageBO();
		workflowQueryPageBO.setProcessKey(bpmnModel.getMainProcess().getId());
		Page<WorkflowBO> workflowBOs = workflowService.findByConds(workflowQueryPageBO);
		log.info("ALM_WORKFLOW中查到个条数: " + workflowBOs.getPageData().size());
		if (CollectionUtils.isEmpty(workflowBOs.getPageData())) {
			log.error("无法在ALM_WORKFLOW中查到相应的流程配置: PROCESS_KEY=" + bpmnModel.getMainProcess().getId());
			return null;
		}
		
		if (!ActivtiAnalysisUtil.proceeDirtyData(bpmnModel.getMainProcess().getFlowElementMap(), workflowBOs.getPageData())) {
			return null;
		}
		WorkflowTemplateQueryPageBO WorkflowTemplateQueryPageBO = new WorkflowTemplateQueryPageBO();
		WorkflowTemplateQueryPageBO.setProcessKey(bpmnModel.getMainProcess().getId());
		Page<WorkflowTemplateBO> workflowTemplateBOs = workflowTemplateService.findByConds(WorkflowTemplateQueryPageBO);
		log.info("ALM_WORKFLOW_TEMPLATE中查到个条数: " + workflowTemplateBOs.getPageData().size());
		if (CollectionUtils.isEmpty(workflowTemplateBOs.getPageData())) {
			log.error("无法在ALM_WORKFLOW_TEMPLATE中查到相应的流程配置: PROCESS_KEY=" + bpmnModel.getMainProcess().getId());
			return null;
		}
		if (workflowTemplateBOs.getPageData().size() != 1) {
			log.error("无法在ALM_WORKFLOW_TEMPLATE中模糊查到唯一的流程配置: PROCESS_KEY=" + bpmnModel.getMainProcess().getId());
			return null;
		}
		ProcessGenerateModel processGenerateModel = new ProcessGenerateModel(workflowBOs.getPageData(), workflowTemplateBOs.getPageData().get(0), bpmnModel);
		if (!CollectionUtils.isEmpty(tableNames)) {
			processGenerateModel.setTables(tableNames);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			log.info(objectMapper.writeValueAsString(bpmnModel));
			log.info(objectMapper.writeValueAsString(processGenerateModel));
		} catch (JsonProcessingException e) {
			
		}
		Map<String, Object> processModel = new HashMap<String, Object>();
		processModel.put("className", StringHelper.changeFirstCharacterCase(processGenerateModel.getWorkflowTemplate().getProcessKey(), true));
		processModel.put("fileName", fileName);
		//log.info(bpmnModel.toString());
		try {
			generatorFacade.deleteByProcessDiagram(processGenerateModel, "src/main/resources/processtemplates", processModel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			generatorFacade.generateByProcessDiagram(processGenerateModel, "src/main/resources/processtemplates", processModel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "succes";
	}

	@Override
	public void generateCrud(List<String> tableNames) {
		// TODO 生成增删该查
		// 打开文件夹
		//Runtime.getRuntime().exec("cmd.exe /c start "+GeneratorProperties.getRequiredProperty("outRoot"));
		try {
			//generatorFacade.printAllTableNames();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
        	for(String tableName : tableNames) {
            	generatorFacade.deleteByTable(tableName,"src/main/resources/crudtemplates");
        	}
        	//GeneratorFacade.printAllTableNames();
			//generatorFacade.deleteOutRootDir();
			//generatorFacade.deleteByTable("ALM_XJ_EXPENSES_NEW_CONTENT","src/main/resources/crudtemplates");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
        	for(String tableName : tableNames) {
	        	generatorFacade.generateByTable(tableName,"src/main/resources/crudtemplates");
	    	}
			//generatorFacade.generateByAllTable("src/main/resources/crudtemplates");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //自动搜索数据库中的所有表并生成文件,template为模板的根目录
        //generatorFacade.generateByClass(BlogeneratorFacade.class,"template_clazz");
        //  generatorFacade.deleteByTable("table_name", "src/main/resources/crudtemplates");
        //删除生成的文件
		
		log.info(generateProperties.toString());
		
	}
	
	@Override
	public boolean processConfigExist(String processKey) throws Exception {
		WorkflowQueryPageBO workflowQueryPageBO = new WorkflowQueryPageBO();
		workflowQueryPageBO.setProcessKey(processKey);
		Page<WorkflowBO> workflowBOs= workflowService.findByConds(workflowQueryPageBO);
		return !CollectionUtils.isEmpty(workflowBOs.getPageData());
	}

	@Override
	public boolean processTemplateExist(String processKey) throws Exception {
		WorkflowTemplateQueryPageBO workflowTemplateQueryPageBO = new WorkflowTemplateQueryPageBO();
		workflowTemplateQueryPageBO.setProcessKey(processKey);
		Page<WorkflowTemplateBO> workflowTemplateBOs =workflowTemplateService.findByConds(workflowTemplateQueryPageBO);
		return !CollectionUtils.isEmpty(workflowTemplateBOs.getPageData());
	}
	
	private boolean checkExist(String processKey) throws Exception {
		if (this.processConfigExist(processKey)) {
			log.error("错误信息: ALM_WORKFLOW中已经存在名称相似的流程主键");
			log.error("解决方案: 如果是更新流程请删除已存在的数据, 如果是流程主键命名重复, 请修改流程主键名称");
			return true;
		}
		if (this.processTemplateExist(processKey)) {
			log.error("错误信息: ALM_WORKFLOW_TEMPLATE中已经存在名称相似的流程主键");
			log.error("解决方案: 如果是更新流程请删除已存在的数据, 如果是流程主键命名重复, 请修改流程主键名称");
			return true;
		}
		return false;
	}

	@Override
	public WorkflowTemplateBO template(String fileName) throws Exception {
		ResourceStreamSource resourceStreamSource = new ResourceStreamSource("process/"+fileName+".bpmn");
		BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
		BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(resourceStreamSource, true, true);
		WorkflowTemplateBO workflowTemplateBO = new WorkflowTemplateBO();
		workflowTemplateBO.setProcessKey(bpmnModel.getMainProcess().getId());
		workflowTemplateBO.setProcessName(bpmnModel.getMainProcess().getName());
		return workflowTemplateBO;
	}

	@Override
	public void generateCrudSql(String sql) {
		log.error("功能待实现");
	}

	@Override
	public BpmnModel processInfo(String fileName) throws Exception {
		ResourceStreamSource resourceStreamSource = new ResourceStreamSource("process/"+fileName+".bpmn");
		BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
		BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(resourceStreamSource, true, true);
		return bpmnModel;
	}

}
