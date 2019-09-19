package com.zos.activiti.function;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.activiti.dmn.engine.CustomExpressionFunctionRegistry;

public class MyFunctionRegister implements CustomExpressionFunctionRegistry {
	
	// 用于存放方法的集合, key 为自定义的方法名, value 为方法实例
	protected static Map<String, Method> customFunctionConfigurations = new HashMap<String, Method>();

	static {
		// 将方法实例添加到集合中
		Method method = getMethod(MyUtil.class, "testMethod", String.class, Integer.class);
		customFunctionConfigurations.put("fn_testMethod", method);

//        Method saleM = getMethod(Sale.class, "doDiscount", Double.class);
//        customFunctionConfigurations.put("fn_doDiscount", saleM);
	}

	@Override
	public Map<String, Method> getCustomExpressionMethods() {
		return customFunctionConfigurations;
	}

	/**
	 * 根据类和方法等信息，返回方法实例
	 */
	protected static Method getMethod(Class<?> classRef, String methodName, Class<?>... methodParm) {
		try {
			return classRef.getMethod(methodName, methodParm);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

}
