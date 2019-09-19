package com.zos.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserTaskCompleteListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1907929675841816460L;

	@Override
	public void notify(DelegateTask delegateTask) {
		log.info("用户任务完成监听");
	}

}
