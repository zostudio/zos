package com.zos.activiti.delegate;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.FixedValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskAllJobDelegate implements TaskListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3228930611840727399L;
	
	private FixedValue userName;

	@Override
	public void notify(DelegateTask delegateTask) {
		log.info("DelegateTask {}", delegateTask.getId());
		log.info("UserName {}", this.userName.getExpressionText());
	}
	
	public void setUserName(FixedValue userName) {
		this.userName = userName;
	}

}
