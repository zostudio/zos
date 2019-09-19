package com.zos.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserTaskAllListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2675853511857464146L;

	@Override
	public void notify(DelegateTask delegateTask) {
		log.info("用户任务全部监听");
	}

}
