package com.zos.generate.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zos.generate.bo.WorkflowBO;

import lombok.extern.slf4j.Slf4j;

/**
 * 工作流分析工具
 * 
 * @author joker
 *
 */
@Slf4j
public class ActivtiAnalysisUtil {

	/**
	 * 查找人工任务(包括会签人工任务), 开始事件和结束事件
	 * 
	 * @param flowElements 流程元素
	 * @return
	 */
	public static List<FlowElement> getUserTaskAndEvent(Collection<FlowElement> flowElements) {
		if (!ActivtiAnalysisUtil.availableProcess(flowElements)) {
			return new ArrayList<FlowElement>();
		}
		return flowElements.stream().filter(flowElement -> flowElement instanceof StartEvent
				|| flowElement instanceof EndEvent || flowElement instanceof UserTask).collect(Collectors.toList());
	}
	
	/**
	 * 查找人工任务(包括会签人工任务)和开始事件
	 * 
	 * @param flowElements
	 * @return
	 */
	public static List<FlowElement> getUserTaskAndStartEvent(Collection<FlowElement> flowElements) {
		if (!ActivtiAnalysisUtil.availableProcess(flowElements)) {
			return new ArrayList<FlowElement>();
		}
		return flowElements.stream().filter(flowElement -> flowElement instanceof StartEvent
				|| flowElement instanceof UserTask).collect(Collectors.toList());
	}
	
	/**
	 * 查找人工任务(包括会签人工任务)
	 * 
	 * @param flowElements
	 * @return
	 */
	public static List<FlowElement> getUserTask(Collection<FlowElement> flowElements) {
		if (!ActivtiAnalysisUtil.availableProcess(flowElements)) {
			return new ArrayList<FlowElement>();
		}
		return flowElements.stream().filter(flowElement -> flowElement instanceof UserTask).collect(Collectors.toList());
	}
	
	/**
	 * 查找结束事件
	 * 
	 * @param flowElements
	 * @return
	 */
	public static List<FlowElement> getEndEvent(Collection<FlowElement> flowElements) {
		if (!ActivtiAnalysisUtil.availableProcess(flowElements)) {
			return new ArrayList<FlowElement>();
		}
		return flowElements.stream().filter(flowElement -> flowElement instanceof EndEvent).collect(Collectors.toList());
	}
	
	/**
	 * 查找人工任务(包括会签人工任务), 开始事件和结束事件
	 * 
	 * @param flowElements 流程元素
	 * @return
	 */
	public static Map<String, FlowElement> getUserTaskAndEvent(Map<String, FlowElement> flowElements) {
		if (!ActivtiAnalysisUtil.availableProcess(flowElements)) {
			return new HashMap<String, FlowElement>();
		}
		Collection<FlowElement> flowElementCollection = ActivtiAnalysisUtil.flowElementMapToCollection(flowElements);
		flowElementCollection = flowElementCollection.stream().filter(flowElement -> flowElement instanceof StartEvent
				|| flowElement instanceof EndEvent || flowElement instanceof UserTask).collect(Collectors.toList());
		return ActivtiAnalysisUtil.flowElementCollectionToMap(flowElementCollection);
	}
	
	/**
	 * 查找人工任务(包括会签人工任务)和开始事件
	 * 
	 * @param flowElements
	 * @return
	 */
	public static Map<String, FlowElement> getUserTaskAndStartEvent(Map<String, FlowElement> flowElements) {
		if (!ActivtiAnalysisUtil.availableProcess(flowElements)) {
			return new HashMap<String, FlowElement>();
		}
		Collection<FlowElement> flowElementCollection = ActivtiAnalysisUtil.flowElementMapToCollection(flowElements);
		flowElementCollection = flowElementCollection.stream().filter(flowElement -> flowElement instanceof StartEvent
				|| flowElement instanceof UserTask).collect(Collectors.toList());
		return ActivtiAnalysisUtil.flowElementCollectionToMap(flowElementCollection);
	}
	
	private static Collection<FlowElement> flowElementMapToCollection(Map<String, FlowElement> flowElements) {
		List<FlowElement> flowElementList = new ArrayList<FlowElement>();
		Set<String> keys = flowElements.keySet();
		for (String key: keys) {
			flowElementList.add(flowElements.get(key));
		}
		return flowElementList;
	}
	
	private static Map<String, FlowElement> flowElementCollectionToMap(Collection<FlowElement> flowElements) {
		Map<String, FlowElement> flowElementMap = new HashMap<String, FlowElement>();
		for (FlowElement flowElement: flowElements) {
			flowElementMap.put(flowElement.getId(), flowElement);
		}
		return flowElementMap;
	}
	
	private static boolean availableProcess(Map<String, FlowElement> flowElementMap) {
		Collection<FlowElement> flowElementCollection = ActivtiAnalysisUtil.flowElementMapToCollection(flowElementMap);
		return ActivtiAnalysisUtil.availableProcess(flowElementCollection);
	}

	/**
	 * 因业务需要目前只能支持, 开始事件, 结束事件, 人工任务, 并行网关, 排他网关, 线; 其他暂不支持
	 * 
	 * @return
	 */
	private static boolean availableProcess(Collection<FlowElement> flowElements) {
		List<FlowElement> otherFlowElements = flowElements.stream()
				.filter(flowElement -> !(flowElement instanceof SequenceFlow || flowElement instanceof StartEvent
						|| flowElement instanceof EndEvent || flowElement instanceof UserTask
						|| flowElement instanceof ExclusiveGateway || flowElement instanceof ParallelGateway))
				.collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(otherFlowElements)) {
			flowElements.forEach(flowElement -> log
					.error("无法处理的流程元素: {主键: " + flowElement.getId() + ", 名称: " + flowElement.getName() + "}"));
			return false;
		}
		return true;
	}
	
	/**
	 * 获取环节类型
	 * 
	 * @param flowElement
	 * @return
	 */
	public static String getLinkNoType(FlowElement flowElement) {
		if (flowElement instanceof StartEvent) {
			return "mgr";
		} else if (flowElement instanceof EndEvent) {
			return "end";
		} else if (flowElement instanceof UserTask) {
			UserTask userTask = (UserTask) flowElement;
			if (userTask.getLoopCharacteristics() != null) {
				return "sign";
			} else {
				return "user";
			}
		} else {
			log.error("");
			return "";
		}
	}
	
	private static Map<String, WorkflowBO> workflowBOCollectionToMap(Collection<WorkflowBO> workflowBOs) {
		Map<String, WorkflowBO> workflowBOMap = new HashMap<String, WorkflowBO>();
		for (WorkflowBO workflowBO: workflowBOs) {
			workflowBOMap.put(workflowBO.getVmTaskNo(), workflowBO);
		}
		return workflowBOMap;
	}
	
	/**
	 * 检查是否含有脏数据, 数据库中的数据必须是流程图中数据的超集
	 * <pre>
	 * true 数据可用, false 数据不可用
	 * </pre>
	 * @param flowElements
	 * @param workflowBOs
	 * @return
	 */
	public static boolean proceeDirtyData(Map<String, FlowElement> flowElements, Collection<WorkflowBO> workflowBOs) {
		flowElements = ActivtiAnalysisUtil.getUserTaskAndEvent(flowElements);
		Collection<FlowElement> flowElementCollection = ActivtiAnalysisUtil.flowElementMapToCollection(flowElements);
		if (flowElementCollection.size() > workflowBOs.size()) {
			log.error("ALM_WORKFLOW中的数据不完整(少于图), 请重新执行第一步操作");
			return false;
		}
		Map<String, WorkflowBO> workflowBOMap = ActivtiAnalysisUtil.workflowBOCollectionToMap(workflowBOs);
		List<FlowElement> flowElementList = flowElementCollection.stream().filter(flowElement -> !workflowBOMap.containsKey(flowElement.getId())).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(flowElementList)) {
			flowElementList.forEach(flowElement -> log
					.error("ALM_WORKFLOW中没有记录的流程元素: {主键: " + flowElement.getId() + ", 名称: " + flowElement.getName() + "}"));
			return false;
		}
		return true;
	}
	
	/**
	 * 分析下个人工任务或结束事件
	 * 
	 * @param targetFlowElement
	 * @param flowElements
	 * @return
	 */
	public static void analysisNextFlowElement(FlowElement targetFlowElement, Map<String, FlowElement> flowElements, List<FlowElement> nextFlowElements) {
		if (targetFlowElement instanceof UserTask || targetFlowElement instanceof EndEvent) {
			nextFlowElements.add(targetFlowElement);
		} else {
			List<SequenceFlow> sequenceFlows = null;
			if (targetFlowElement instanceof ExclusiveGateway) {
				sequenceFlows = ((ExclusiveGateway)targetFlowElement).getOutgoingFlows();
			} else if (targetFlowElement instanceof ParallelGateway) {
				sequenceFlows = ((ParallelGateway)targetFlowElement).getOutgoingFlows();
			}
			if (sequenceFlows != null) {
				for(SequenceFlow sequenceFlow : sequenceFlows) {
					if (!StringUtils.isBlank(sequenceFlow.getConditionExpression())) {
						analysisNextFlowElement(flowElements.get(sequenceFlow.getTargetRef()), flowElements, nextFlowElements);
					} else {
						log.error("流程图的非人工任务后出现条件表达式: "+ActivtiAnalysisUtil.getObjectToJson(sequenceFlow));
					}
				}
			}
		}
	}
	
	/**
	 * 分析下个人工任务或结束事件
	 * 
	 * @param targetFlowElement
	 * @param flowElements
	 * @return
	 */
	public static void analysisNextOutGoingFlowMap(SequenceFlow sourceSequenceFlow, Map<String, FlowElement> flowElements, Map<String, List<FlowElement>> nextOutGoingFlowMap) {
		if (nextOutGoingFlowMap == null) {
			nextOutGoingFlowMap = new HashMap<String, List<FlowElement>>();
		}
		List<FlowElement> nextFlowElements = null;
		if (flowElements.get(sourceSequenceFlow.getTargetRef()) instanceof UserTask || flowElements.get(sourceSequenceFlow.getTargetRef()) instanceof EndEvent) {
			if (nextOutGoingFlowMap.containsKey(sourceSequenceFlow.getConditionExpression())) {
				nextFlowElements = nextOutGoingFlowMap.get(sourceSequenceFlow.getConditionExpression());
			} else {
				nextFlowElements = new ArrayList<FlowElement>();
			}
			nextFlowElements.add(flowElements.get(sourceSequenceFlow.getTargetRef()));
			nextOutGoingFlowMap.put(sourceSequenceFlow.getConditionExpression(), nextFlowElements);
		} else {
			List<SequenceFlow> sequenceFlows = null;
			if (flowElements.get(sourceSequenceFlow.getTargetRef()) instanceof ExclusiveGateway) {
				sequenceFlows = ((ExclusiveGateway)flowElements.get(sourceSequenceFlow.getTargetRef())).getOutgoingFlows();
			} else if (flowElements.get(sourceSequenceFlow.getTargetRef()) instanceof ParallelGateway) {
				sequenceFlows = ((ParallelGateway)flowElements.get(sourceSequenceFlow.getTargetRef())).getOutgoingFlows();
			}
			if (!CollectionUtils.isEmpty(sequenceFlows)) {
				for(SequenceFlow sequenceFlow : sequenceFlows) {
					analysisNextOutGoingFlowMap(sequenceFlow, flowElements, nextOutGoingFlowMap);
				}
			}
		}
	}
	
	public static String getObjectToJson(Object value) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}
		return "无法将对象转换为JSON.";
	}

}
