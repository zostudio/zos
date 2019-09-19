package com.zos.generate.generate.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.zos.generate")
public class GenerateProperties {
	
	/**
	 * 包名 com.zos.generate
	 */
	private String basePackage;
	
	/**
	 * 流程名
	 */
	private String process;
	
	/**
	 * 名称空间
	 */
	private String namespace;
	
	/**
	 * 输出文件夹
	 */
	private String outUri;
	
	/**
	 * 生成时需要省略的表名称前缀部分, act_workflow如果想要生成的名称是workflow则需要配置act_, 多个值用逗号隔开
	 */
	private String tableRemovePrefixes = "";
	
	/**
	 * 类型转换
	 */
	private List<TypeMappingPropertie> typeMappings;
	
	private JdbcProperties jdbc;
	
}
