package com.zos.generate.common;

import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskListener {
	
	private String type;
	
	private String event;
	
	private String implementationType;
	
	private String implementation;
	
	@SuppressWarnings("unused")
	private TaskListener() {}
	
	public TaskListener(String event, String implementationType, String implementation, String type) {
		this.setEvent(event);
		this.setImplementationType(implementationType);
		this.setImplementation(implementation);
		this.type = type;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		if ("create".equals(event) || "delete".equals(event) || "end".equals(event)) {
			this.event = event;
		} else {
			this.event = "";
			log.error("监听事件类型没有正确配置: "+event);
		}
		
	}

	public String getImplementationType() {
		return implementationType;
	}

	public void setImplementationType(String implementationType) {
		if ("delegateExpression".equals(implementationType)) {
			this.implementationType = implementationType;
		} else {
			this.implementationType = "";
			log.error("监听委派表达式类型没有正确配置: "+implementationType);
		}
	}

	public String getImplementation() {
		return implementation;
	}

	public void setImplementation(String implementation) {
		Pattern pattern = Pattern.compile("^#\\{.*\\}$");
		if (pattern.matcher(implementation).matches()) {
			this.implementation = implementation.substring(implementation.indexOf("{")+1, implementation.indexOf("}"));
		} else {
			this.implementation = "";
			log.error("监听委派表达式没有正确配置: "+implementation);
		}
	}

	@Override
	public String toString() {
		return "TaskListener [event=" + event + ", implementationType=" + implementationType + ", implementation="
				+ implementation + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
