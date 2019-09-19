package com.zos.generate.common;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.zos.generate.bo.WorkflowBO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutGoingFlow{
	
	private String conditionExpression;
	
	private List<WorkflowBO> workflowBOs;

	public String getConditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(String conditionExpression) {
		if (StringUtils.isBlank(conditionExpression)) {
			this.conditionExpression = "";
		} else if (conditionExpression.contains("'")){
			String[] exp = conditionExpression.split("'");
			if (exp.length == 3) {
				this.conditionExpression = exp[1];
			} else {
				log.error("无法正确解析的条件表达式: " + conditionExpression);
				this.conditionExpression = conditionExpression;
			}
		} else {
			this.conditionExpression = conditionExpression;
		}
	}

	public List<WorkflowBO> getWorkflowBOs() {
		return workflowBOs;
	}

	public void setWorkflowBOs(List<WorkflowBO> workflowBOs) {
		this.workflowBOs = workflowBOs;
	}

	public OutGoingFlow(List<WorkflowBO> workflowBOs) {
		this.workflowBOs = workflowBOs;
	}

	public OutGoingFlow(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}

	public OutGoingFlow(String conditionExpression, List<WorkflowBO> workflowBOs) {
		this.conditionExpression = conditionExpression;
		this.workflowBOs = workflowBOs;
	}

	public OutGoingFlow() {}
	
}
