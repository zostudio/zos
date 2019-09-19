/**
 * 
 */
package com.zos.activiti.delegate;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 01Studio
 *
 */
@Slf4j
public class BoundaryErrorJobDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {
		log.info("this is boundary error");
		throw new BpmnError("boundaryError");
	}

}
