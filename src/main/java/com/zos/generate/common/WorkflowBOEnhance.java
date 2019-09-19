package com.zos.generate.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.zos.generate.bo.WorkflowBO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkflowBOEnhance extends WorkflowBO {

	private List<OutGoingFlow> outGoingFlows;
	
	public List<OutGoingFlow> getOutGoingFlows() {
		return outGoingFlows;
	}

	public void setOutGoingFlows(List<SequenceFlow> outGoingFlows, Map<String, WorkflowBO> workflowBOMap, Map<String, FlowElement> flowElements) {
		List<OutGoingFlow> result = new ArrayList<OutGoingFlow>();
		for (SequenceFlow sequenceFlow : outGoingFlows) {
			List<WorkflowBO> workflowBOList = new ArrayList<WorkflowBO>();
			if (workflowBOMap.containsKey(sequenceFlow.getTargetRef())) {
				workflowBOList.add(workflowBOMap.get(sequenceFlow.getTargetRef()));
				OutGoingFlow outGoingFlow = new OutGoingFlow(workflowBOList);
				this.setConditionExpression(outGoingFlow, sequenceFlow);
				result.add(outGoingFlow);
			} else {
				// 不是数据库模型中的流程元素, 从流程图中抓取流程元素, 分析流转方向
				List<FlowElement> nextFlowElements = new ArrayList<FlowElement>();
				ActivtiAnalysisUtil.analysisNextFlowElement(flowElements.get(sequenceFlow.getTargetRef()), flowElements, nextFlowElements);
				Map<String, List<FlowElement>> nextOutGoingFlowMap = new HashMap<String, List<FlowElement>>();
				ActivtiAnalysisUtil.analysisNextOutGoingFlowMap(sequenceFlow, flowElements, nextOutGoingFlowMap);
				if (!CollectionUtils.isEmpty(nextOutGoingFlowMap)) {
					for (String condition : nextOutGoingFlowMap.keySet()) {
						for (FlowElement nextFlowElement : nextOutGoingFlowMap.get(condition)) {
							if (workflowBOMap.containsKey(nextFlowElement.getId())) {
								workflowBOList.add(workflowBOMap.get(nextFlowElement.getId()));
							} else {
								log.error("分析出来的下个人工任务或结束事件在ALM_WORKFLOW中不存在: "+ActivtiAnalysisUtil.getObjectToJson(nextFlowElement));
							}
						}
						OutGoingFlow outGoingFlow = new OutGoingFlow(workflowBOList);
						outGoingFlow.setConditionExpression(condition);
						result.add(outGoingFlow);
						workflowBOList = new ArrayList<WorkflowBO>();
					}
				}
			}
		}
		this.outGoingFlows = result;
	}
	
	private void setConditionExpression(OutGoingFlow outGoingFlow, SequenceFlow sequenceFlow) {
		if (StringUtils.isBlank(sequenceFlow.getConditionExpression())) {
			outGoingFlow.setConditionExpression("");
		} else {
			outGoingFlow.setConditionExpression(sequenceFlow.getConditionExpression());
		}
	}

	public WorkflowBOEnhance() {
		super();
	}
	
	public WorkflowBOEnhance(List<OutGoingFlow> outGoingFlows) {
		super();
		this.outGoingFlows = outGoingFlows;
	}

	public WorkflowBOEnhance(Long wfItemId, String processKey, String phaseId, String phaseName, String vmTaskName,
			String vmTaskNo, Long linkId, String linkNo, String linkNoType, String roleCode, String linkUrl,
			Boolean isPrint, String canBack, String backBtn) {
		super(wfItemId, processKey, phaseId, phaseName, vmTaskName, vmTaskNo, linkId, linkNo, linkNoType, roleCode, linkUrl,
				isPrint, canBack, backBtn);
	}

	public WorkflowBOEnhance(Long wfItemId, String processKey, String phaseId, String phaseName, String vmTaskName,
			String vmTaskNo, Long linkId, String linkNo, String linkNoType, String roleCode, String linkUrl,
			Boolean isPrint, String canBack, String backBtn, List<OutGoingFlow> outGoingFlows) {
		super(wfItemId, processKey, phaseId, phaseName, vmTaskName, vmTaskNo, linkId, linkNo, linkNoType, roleCode, linkUrl,
				isPrint, canBack, backBtn);
		this.outGoingFlows = outGoingFlows;
	}
	
	public boolean getIsUserTask() {
		if ("user".equals(super.getLinkNoType().trim())) {
			return true;
		}
		return false;
	}
	
	public boolean getIsSignUserTask() {
		if ("sign".equals(super.getLinkNoType().trim())) {
			return true;
		}
		return false;
	}
	
	public boolean getIsStartEvent() {
		if ("mgr".equals(super.getLinkNoType().trim())) {
			return true;
		}
		return false;
	}
	
	public boolean getIsEndEvent() {
		if ("end".equals(super.getLinkNoType().trim())) {
			return true;
		}
		return false;
	}
}
