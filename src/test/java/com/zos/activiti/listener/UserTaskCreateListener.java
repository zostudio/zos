package com.zos.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.JuelExpression;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskCreateListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7648853064382271549L;

	private JuelExpression userName;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		log.info("用户任务创建监听");
		if (userName != null) {
			log.info("UserName {}", this.userName.getExpressionText());
		}
	}
	
	public void setUserName(JuelExpression userName) {
		this.userName = userName;
	}

}
