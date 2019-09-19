package com.zos.generate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 
 * @author joker
 */
@ApiModel(description = "")
public class WorkflowTemplateQueryPageDTO {
	
	@ApiModelProperty(value = "(开始)")
	private java.lang.Long template_id_begin;
	
	@ApiModelProperty(value = "(结束)")
	private java.lang.Long template_id_end;
	
	@ApiModelProperty(value = "流程主键")
	private java.lang.String process_key;
	
	@ApiModelProperty(value = "流程类型")
	private java.lang.String obj_type;
	
	@ApiModelProperty(value = "流程名称")
	private java.lang.String process_name;
	
	@ApiModelProperty(value = "分页页码")
	private Integer page_no;
	
	@ApiModelProperty(value = "分页步长")
	private Integer page_size;
	public java.lang.Long getTemplate_id_begin() {
		return this.template_id_begin;
	}
	
	public void setTemplate_id_begin(java.lang.Long value) {
		this.template_id_begin = value;
	}
	
	public java.lang.Long getTemplate_id_end() {
		return this.template_id_end;
	}
	
	public void setTemplate_id_end(java.lang.Long value) {
		this.template_id_end = value;
	}
	public java.lang.String getProcess_key() {
		return this.process_key;
	}

	public void setProcess_key(java.lang.String value) {
		this.process_key = value;
	}	    	
	public java.lang.String getObj_type() {
		return this.obj_type;
	}

	public void setObj_type(java.lang.String value) {
		this.obj_type = value;
	}	    	
	public java.lang.String getProcess_name() {
		return this.process_name;
	}

	public void setProcess_name(java.lang.String value) {
		this.process_name = value;
	}	    	
	
	public Integer getPage_no() {
		return this.page_no;
	}
	
	public Integer getPage_size() {
		return this.page_size;
	}
	
	public void setPage_no(Integer page_no) {
		this.page_no = page_no;
	}
	
	public void setPage_size(Integer page_size) {
		this.page_size = page_size;
	}
}

