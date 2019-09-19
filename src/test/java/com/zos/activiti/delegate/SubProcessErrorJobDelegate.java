package com.zos.activiti.delegate;

import java.io.Serializable;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubProcessErrorJobDelegate implements JavaDelegate, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1363850843331484450L;

	@Override
	public void execute(DelegateExecution execution) {
		log.info("DelegateExecution {}", execution.getId());
		throw new BpmnError("subProcessErrorError");
	}

}
