/**
 * 
 */
package com.zos.activiti.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 01Studio
 *
 */
@Slf4j
public class MultipleActivitiJobDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {
		log.info("DelegateExecution {}", execution.getCurrentActivityId());
		if (execution.getVariable("data") != null) {
			log.info("data {}", execution.getVariable("data"));
		}
		if (execution.getVariable("data3") != null) {
			log.info("data3 {}", execution.getVariable("data3"));
		}
		if (execution.getVariable("data4") != null) {
			log.info("data4 {}", execution.getVariable("data4"));
		}
	}

}
