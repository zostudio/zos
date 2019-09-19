/**
 * 
 */
package com.zos.activiti.beans;

import java.io.Serializable;

import org.activiti.engine.runtime.Execution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 01Studio
 *
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class MyServiceTaskBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2452298324552854983L;
	
	private String name = "joker";
	
	public void printf(Execution execution) {
		log.info("Execution ActivityId {}", execution.getActivityId());
		log.info("name {}", this.getName());
	}
}
