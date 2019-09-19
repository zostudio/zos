package com.zos.generate.service;

import java.util.List;

import org.activiti.bpmn.model.BpmnModel;

import com.zos.generate.bo.WorkflowBO;
import com.zos.generate.bo.WorkflowTemplateBO;

public interface IGenerateService {
	
	/**
	 * 处理人工任务
	 */
	public void userTask();
	
	/**
	 * 处理会签任务
	 */
	public void signUserTask();
	
	/**
	 * 处理开始事件
	 */
	public void startEvent();
	
	/**
	 * 处理结束事件
	 */
	public void endEvent();
	
	/**
	 * 处理流程配置
	 */
	public List<WorkflowBO> workflow(String fileName, Long startId) throws Exception;
	
	/**
	 * 处理流程配置
	 */
	public WorkflowTemplateBO template(String fileName) throws Exception;
	
	/**
	 * 生成REST接口
	 */
	public String generateRest(String fileName, List<String> tableNames) throws Exception;
	
	/**
	 * 生成增删该查
	 */
	public void generateCrud(List<String> tableNames);
	
	/**
	 * 生成增删该查
	 */
	public void generateCrudSql(String sql);
	
	/**
	 * 根据流程主键检测流程配置是否已经存在
	 * 
	 * @param processKey
	 * @return
	 */
	public boolean processConfigExist(String processKey) throws Exception;
	
	/**
	 * 根据流程主键检测流程模版是否已经存在
	 * 
	 * @param processKey
	 * @return
	 */
	public boolean processTemplateExist(String processKey) throws Exception;
	
	public BpmnModel processInfo(String fileName) throws Exception;
	
}
