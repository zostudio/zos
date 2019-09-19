package com.zos.activiti.beans;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateTask;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyUserTaskAllBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5161782371878744148L;

	public void testBean(DelegateTask delegateTask) {
		log.info("DelegateTask {}", delegateTask.getName());
	}
}
