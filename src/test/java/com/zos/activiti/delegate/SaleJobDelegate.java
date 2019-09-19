/**
 * 
 */
package com.zos.activiti.delegate;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.zos.activiti.drools.Member;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 01Studio
 *
 */
@Slf4j
public class SaleJobDelegate implements JavaDelegate {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) {
		log.info("DelegateExecution {}", execution.getCurrentActivityId());
		List<Member> members = (List<Member>) execution.getVariable("result");
		for (Member member :  members) {
			log.info("Member {}, {}, {}", member.getIdentity(), member.getAmount(), member.getResult());
		}
	}

}
