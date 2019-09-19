package com.zos.generate.generate.provider.java.model;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.zos.generate.generate.util.typemapping.ActionScriptDataTypesUtils;
import com.zos.generate.generate.util.typemapping.JavaPrimitiveTypeMapping;

public class JavaProperty {

	PropertyDescriptor propertyDescriptor;

	// 与 property 相关联的 class
	JavaClass clazz;

	public JavaProperty(PropertyDescriptor pd, JavaClass javaClass) {
		this.propertyDescriptor = pd;
		this.clazz = javaClass;
	}

	public String getName() {
		return propertyDescriptor.getName();
	}

	public String getJavaType() {
		return propertyDescriptor.getPropertyType().getName();
	}

	public String getPrimitiveJavaType() {
		return JavaPrimitiveTypeMapping.getPrimitiveType(getJavaType());
	}

	public JavaClass getPropertyType() {
		return new JavaClass(propertyDescriptor.getPropertyType());
	}

	public String getDisplayName() {
		return propertyDescriptor.getDisplayName();
	}

	public JavaMethod getReadMethod() {
		return new JavaMethod(propertyDescriptor.getReadMethod(), clazz);
	}

	public JavaMethod getWriteMethod() {
		return new JavaMethod(propertyDescriptor.getWriteMethod(), clazz);
	}

	public String getAsType() {
		return ActionScriptDataTypesUtils.getPreferredAsType(propertyDescriptor.getPropertyType().getName());
	}

	public boolean isPk() {
		return JPAUtils.isPk(propertyDescriptor.getReadMethod());
	}

	public JavaClass getClazz() {
		return clazz;
	}

	@Override
	public String toString() {
		return "JavaClass:" + clazz + " JavaProperty:" + getName();
	}

	public static class JPAUtils {
		private static boolean isJPAClassAvaiable = false;
		static {
			try {
				Class.forName("javax.persistence.Table");
				isJPAClassAvaiable = true;
			} catch (ClassNotFoundException e) {
			}
		}

		@SuppressWarnings("unchecked")
		public static boolean isPk(Method readMethod) {
			if (isJPAClassAvaiable) {
				if (readMethod != null && readMethod.isAnnotationPresent((Class<? extends Annotation>) classForName("javax.persistence.Id"))) {
					return true;
				}
			}
			return false;
		}

		private static Class<?> classForName(String clazz) {
			try {
				return Class.forName(clazz);
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
	}
}
