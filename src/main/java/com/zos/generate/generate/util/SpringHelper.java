package com.zos.generate.generate.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringHelper implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext = null; 

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringHelper.applicationContext == null) {
			SpringHelper.applicationContext = applicationContext;
        }
	}
	
	public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
  
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

}
