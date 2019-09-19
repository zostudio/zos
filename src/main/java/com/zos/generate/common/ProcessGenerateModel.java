package com.zos.generate.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.zos.generate.bo.WorkflowBO;
import com.zos.generate.bo.WorkflowTemplateBO;
import com.zos.generate.generate.provider.db.table.TableFactory;
import com.zos.generate.generate.provider.db.table.model.Table;
import com.zos.generate.mapper.WorkflowMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 流程生成模型
 * 
 * @author 01Studio
 *
 */
@Slf4j
public class ProcessGenerateModel {

	// 工作流增强模型(人工(含会签)和开始事件)
	private List<WorkflowBOEnhance> workflowEnhances;
	
	// 工作流增强模型(人工(含会签), 开始事件和结束事件)
	private List<WorkflowBOEnhance> workflows;
	
	// 工作流模版
	private WorkflowTemplateBO workflowTemplate;
	
	// 工作流监听
	private List<TaskListener> taskListeners;
	
	private List<Table> tables;
	
	private Map<String, WorkflowBO> workflowBOCollectionToMap(Collection<WorkflowBO> workflowBOs) {
		Map<String, WorkflowBO> workflowBOMap = new HashMap<String, WorkflowBO>();
		for (WorkflowBO workflowBO: workflowBOs) {
			workflowBOMap.put(workflowBO.getVmTaskNo(), workflowBO);
		}
		return workflowBOMap;
	}
	
	public List<Table> getTables() {
		return tables;
	}
	
	public Integer getTablesCount() {
		return this.tables == null ? 0 : this.tables.size();
	}

	public void setTables(List<String> tableNames) {
		List<Table> tables = null;
		if (this.tables == null) {
			tables = new ArrayList<Table>();
		} else {
			tables = this.tables;
		}
		for (String tableName : tableNames) {
			Table table = TableFactory.getInstance().getTable(tableName);
			tables.add(table);
		}
		this.tables = tables;
	}
	
	private void analysisTaskListener(BpmnModel bpmnModel, Boolean hasSignUserTask) {
		List<FlowElement> userTaskFlowElementList = ActivtiAnalysisUtil.getUserTask(bpmnModel.getMainProcess().getFlowElements());
		List<FlowElement> endEventFlowElementList = ActivtiAnalysisUtil.getEndEvent(bpmnModel.getMainProcess().getFlowElements());
		for (FlowElement userTask : userTaskFlowElementList) {
			List<ActivitiListener> taskListeners = ((UserTask) userTask).getTaskListeners();
			if (CollectionUtils.isEmpty(taskListeners)) {
				log.error("人工任务: "+ActivtiAnalysisUtil.getObjectToJson(userTask)+", 没有配置监听");
			} else {
				this.addTaskListener(taskListeners, userTask);
			}
		}
		for (FlowElement endEvent : endEventFlowElementList) {
			List<ActivitiListener> taskListeners = ((EndEvent) endEvent).getExecutionListeners();
			if (CollectionUtils.isEmpty(taskListeners)) {
				log.error("结束事件: "+ActivtiAnalysisUtil.getObjectToJson(endEvent)+", 没有配置监听");
			} else {
				this.addTaskListener(taskListeners, endEvent);
			}
		}
		if (hasSignUserTask && this.taskListeners.size() != 5) {
			log.error("流程中配置的监听有问题(人工前置,人工后置,会签前置,会签后置,结束), 请仔细检查: "+ActivtiAnalysisUtil.getObjectToJson(this.taskListeners));
		} else if (!hasSignUserTask && this.taskListeners.size() != 3) {
			log.error("流程中配置的监听有问题(人工前置,人工后置,结束), 请仔细检查: "+ActivtiAnalysisUtil.getObjectToJson(this.taskListeners));
		}
	}
	
	private void addTaskListener(List<ActivitiListener> activitiListeners, FlowElement flowElement) {
		for (ActivitiListener activitiListener : activitiListeners) {
			if (CollectionUtils.isEmpty(this.taskListeners)) {
				this.taskListeners = new ArrayList<TaskListener>();
			}
			if (StringUtils.isBlank(activitiListener.getEvent())) {
				log.error(ActivtiAnalysisUtil.getObjectToJson(flowElement)+", 没有配置监听事件类型");
			}
			if (StringUtils.isBlank(activitiListener.getImplementationType())) {
				log.error(ActivtiAnalysisUtil.getObjectToJson(flowElement)+", 没有配置监听委派表达式类型");
			}
			if (StringUtils.isBlank(activitiListener.getImplementation())) {
				log.error(ActivtiAnalysisUtil.getObjectToJson(flowElement)+", 没有配置监听委派表达式");
			}
			TaskListener taskListener = new TaskListener(activitiListener.getEvent().trim(), activitiListener.getImplementationType().trim(), activitiListener.getImplementation().trim(), ActivtiAnalysisUtil.getLinkNoType(flowElement));
			Optional<TaskListener> findTaskListener= this.taskListeners.stream().filter(item -> item.toString().equals(taskListener.toString())).findFirst();
			if (!findTaskListener.isPresent()) {
				this.taskListeners.add(taskListener);
			}
		}
	}

	private void outgoingFlows(BpmnModel bpmnModel, List<WorkflowBO> workflowBOs) {
		List<FlowElement> flowElementList = ActivtiAnalysisUtil.getUserTaskAndStartEvent(bpmnModel.getMainProcess().getFlowElements());
		Map<String, WorkflowBO> workflowBOMap = this.workflowBOCollectionToMap(workflowBOs);
		List<WorkflowBOEnhance> workflowEnhanceList = new ArrayList<WorkflowBOEnhance>();
		for (FlowElement flowElement : flowElementList) {
			WorkflowBOEnhance workflowBOEnhance = WorkflowMapper.INSTANCE.botoEnhance(workflowBOMap.get(flowElement.getId()));
			if (flowElement instanceof UserTask) {
				workflowBOEnhance.setOutGoingFlows(((UserTask) flowElement).getOutgoingFlows(), workflowBOMap, bpmnModel.getMainProcess().getFlowElementMap());
			} else if (flowElement instanceof StartEvent) {
				workflowBOEnhance.setOutGoingFlows(((StartEvent) flowElement).getOutgoingFlows(), workflowBOMap, bpmnModel.getMainProcess().getFlowElementMap());
			} else {
				log.warn("生成Freemarker模型时, 无法识别的流程图上的非人工(含会签)或开始节点: ID: "+flowElement.getId()+", Name: "+flowElement.getName());
				continue;
			}
			workflowEnhanceList.add(workflowBOEnhance);
		}
		this.workflowEnhances = workflowEnhanceList;
		flowElementList = ActivtiAnalysisUtil.getEndEvent(bpmnModel.getMainProcess().getFlowElements());
		workflowEnhanceList = new ArrayList<WorkflowBOEnhance>();
		workflowEnhanceList.addAll(this.workflowEnhances);
		for (FlowElement flowElement : flowElementList) {
			WorkflowBOEnhance workflowBOEnhance = WorkflowMapper.INSTANCE.botoEnhance(workflowBOMap.get(flowElement.getId()));
			workflowEnhanceList.add(workflowBOEnhance);
		}
		this.workflows = workflowEnhanceList;
	}
	
	private boolean hasSignUserTask(List<WorkflowBO> workflowBOs) {
		for (WorkflowBO workflowBO : workflowBOs) {
			if ("sign".equals(workflowBO.getLinkNoType().trim())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 开始事件
	 * 
	 * @return
	 */
	public List<WorkflowBOEnhance> getAllStartEvent() {
		List<WorkflowBOEnhance> result = new ArrayList<WorkflowBOEnhance>();
		for (WorkflowBOEnhance workflowEnhance : getWorkflowEnhances()) {
			if (workflowEnhance.getIsStartEvent()) {
				result.add(workflowEnhance);
			}
		}
		return result;
	}
	
	/**
	 * 全部结束事件
	 * 
	 * @return
	 */
	public List<WorkflowBOEnhance> getAllEndEvent() {
		List<WorkflowBOEnhance> result = new ArrayList<WorkflowBOEnhance>();
		for (WorkflowBOEnhance workflowEnhance : getWorkflows()) {
			if (workflowEnhance.getIsEndEvent()) {
				result.add(workflowEnhance);
			}
		}
		return result;
	}
	
	/**
	 * 结束事件个数
	 * 
	 * @return
	 */
	public int getEndEventCount() {
		int count  = 0;
		for (WorkflowBOEnhance workflowEnhance : getWorkflows()) {
			if (workflowEnhance.getIsEndEvent()) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 全部流程增强模型
	 * 
	 * @return
	 */
	public List<WorkflowBOEnhance> getAllEnhance() {
		return getWorkflowEnhances();
	}
	
	public List<WorkflowBOEnhance> getAllUserEnhance() {
		List<WorkflowBOEnhance> result = new ArrayList<WorkflowBOEnhance>();
		for (WorkflowBOEnhance workflowBOEnhance : getWorkflowEnhances()) {
			if (workflowBOEnhance.getIsUserTask() || workflowBOEnhance.getIsSignUserTask()) {
				result.add(workflowBOEnhance);
			}
		}
		return result;
	}
	
	/**
	 * 流程主键(首字母大写)
	 * @return
	 */
	public String getProcessKeyUpperCase() {
		return this.getWorkflowTemplate().getProcessKey().toUpperCase();
	}
	
	/**
	 * 流程名称
	 * 
	 * @return
	 */
	public String getProcessName() {
		return this.getWorkflowTemplate().getProcessName();
	}
	
	/**
	 * 流程主键
	 * 
	 * @return
	 */
	public String getProcessKey() {
		return this.getWorkflowTemplate().getProcessKey();
	}
	
	/**
	 * 流程类型
	 * 
	 * @return
	 */
	public String getObjType() {
		return this.getWorkflowTemplate().getObjType();
	}
	
	/**
	 * 流程配置阶段信息
	 * 
	 * @return
	 */
	public Map<String, String> getPhases() {
		Map<String, String> result  = new HashMap<String, String>();
		for (WorkflowBO workflowBO : getWorkflows()) {
			result.put(workflowBO.getPhaseId(), workflowBO.getPhaseName());
		}
		return result;
	}
	
	/*******************************/
	/** 构造方法, Getter 和 Setter
	/*******************************/

	public ProcessGenerateModel(List<WorkflowBO> workflows, WorkflowTemplateBO workflowTemplate,
			BpmnModel bpmnModel) {
		this.workflowTemplate = workflowTemplate;
		this.outgoingFlows(bpmnModel, workflows);
		this.analysisTaskListener(bpmnModel, this.hasSignUserTask(workflows));
	}

	@SuppressWarnings("unused")
	private ProcessGenerateModel() {}

	public List<WorkflowBOEnhance> getWorkflowEnhances() {
		return workflowEnhances;
	}

	public void setWorkflowEnhances(List<WorkflowBOEnhance> workflowEnhances) {
		this.workflowEnhances = workflowEnhances;
	}

	public List<WorkflowBOEnhance> getWorkflows() {
		return workflows;
	}

	public void setWorkflows(List<WorkflowBOEnhance> workflows) {
		this.workflows = workflows;
	}
	
	public WorkflowTemplateBO getWorkflowTemplate() {
		return this.workflowTemplate;
	}

	public void setWorkflowTemplate(WorkflowTemplateBO workflowTemplate) {
		this.workflowTemplate = workflowTemplate;
	}

	public List<TaskListener> getTaskListeners() {
		return taskListeners;
	}
	
	public TaskListener getSignPreListener() {
		for (TaskListener taskListener : this.getTaskListeners()) {
			if ("sign".equals(taskListener.getType()) && "create".equals(taskListener.getEvent())) {
				return taskListener;
			}
		}
		return null;
	}
	
	public TaskListener getSignRareListener() {
		for (TaskListener taskListener : this.getTaskListeners()) {
			if ("sign".equals(taskListener.getType()) && "delete".equals(taskListener.getEvent())) {
				return taskListener;
			}
		}
		return null;
	}
	
	public TaskListener getUserPreListener() {
		for (TaskListener taskListener : this.getTaskListeners()) {
			if ("user".equals(taskListener.getType()) && "create".equals(taskListener.getEvent())) {
				return taskListener;
			}
		}
		return null;
	}
	
	public TaskListener getUserRareListener() {
		for (TaskListener taskListener : this.getTaskListeners()) {
			if ("user".equals(taskListener.getType()) && "delete".equals(taskListener.getEvent())) {
				return taskListener;
			}
		}
		return null;
	}
	
	/**
	 * 返回一个结束监听
	 * 
	 * @return
	 */
	public TaskListener getEndListener() {
		for (TaskListener taskListener : this.getTaskListeners()) {
			if ("end".equals(taskListener.getType()) && "end".equals(taskListener.getEvent())) {
				return taskListener;
			}
		}
		return null;
	}
}
