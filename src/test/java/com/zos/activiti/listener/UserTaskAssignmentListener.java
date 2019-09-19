package com.zos.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserTaskAssignmentListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6332951808714816578L;

	@Override
	public void notify(DelegateTask delegateTask) {
		log.info("用户任务委托监听");
	}

}
