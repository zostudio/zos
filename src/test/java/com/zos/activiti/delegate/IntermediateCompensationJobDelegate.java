package com.zos.activiti.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IntermediateCompensationJobDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {
		log.info("IntermediateCompensationJobDelegate DelegateExecution ProcessInstanceId {}", execution.getProcessInstanceId());
		log.info("ActivityId {}", execution.getCurrentActivityId());
	}

}
