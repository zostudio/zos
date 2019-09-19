package com.zos.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserTaskCandidateListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4336415191094240923L;

	@Override
	public void notify(DelegateTask delegateTask) {
		log.info("Start Listener Task");
		delegateTask.setAssignee("joker");
		delegateTask.addCandidateGroup("management");
		delegateTask.addCandidateGroup("boss");
		delegateTask.addCandidateUser("joker");
	}

}
