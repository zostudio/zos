package com.zos.activiti.delegate;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyServiceTaskJavaDelegate implements JavaDelegate, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5695261167940453564L;

	@Override
	public void execute(DelegateExecution execution) {
		log.info("这是自定义的处理类");
	}

}
