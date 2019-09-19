package com.zos.generate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程模版
 * 
 * @author joker
 */
@ApiModel(description = "流程模版")
public class WorkflowQueryPageDTO {
	
	@ApiModelProperty(value = "模版项ID(开始)")
	private java.lang.Long wf_item_id_begin;
	
	@ApiModelProperty(value = "模版项ID(结束)")
	private java.lang.Long wf_item_id_end;
	
	@ApiModelProperty(value = "流程模版主键")
	private java.lang.String process_key;
	
	@ApiModelProperty(value = "阶段编号")
	private java.lang.String phase_id;
	
	@ApiModelProperty(value = "阶段名称")
	private java.lang.String phase_name;
	
	@ApiModelProperty(value = "任务名称")
	private java.lang.String vm_task_name;
	
	@ApiModelProperty(value = "任务编号")
	private java.lang.String vm_task_no;
	
	@ApiModelProperty(value = "环节ID(开始)")
	private java.lang.Long link_id_begin;
	
	@ApiModelProperty(value = "环节ID(结束)")
	private java.lang.Long link_id_end;
	
	@ApiModelProperty(value = "环节编号")
	private java.lang.String link_no;
	
	@ApiModelProperty(value = "环节类型")
	private java.lang.String link_no_type;
	
	@ApiModelProperty(value = "权限ID")
	private java.lang.String role_code;
	
	@ApiModelProperty(value = "页面链接")
	private java.lang.String link_url;
	
	@ApiModelProperty(value = "是否打单")
	private java.lang.Boolean is_print;
	
	@ApiModelProperty(value = "是否可以回退")
	private java.lang.String can_back;
	
	@ApiModelProperty(value = "回退按钮")
	private java.lang.String back_btn;
	
	@ApiModelProperty(value = "分页页码")
	private Integer page_no;
	
	@ApiModelProperty(value = "分页步长")
	private Integer page_size;
	public java.lang.Long getWf_item_id_begin() {
		return this.wf_item_id_begin;
	}
	
	public void setWf_item_id_begin(java.lang.Long value) {
		this.wf_item_id_begin = value;
	}
	
	public java.lang.Long getWf_item_id_end() {
		return this.wf_item_id_end;
	}
	
	public void setWf_item_id_end(java.lang.Long value) {
		this.wf_item_id_end = value;
	}
	public java.lang.String getProcess_key() {
		return this.process_key;
	}

	public void setProcess_key(java.lang.String value) {
		this.process_key = value;
	}	    	
	public java.lang.String getPhase_id() {
		return this.phase_id;
	}

	public void setPhase_id(java.lang.String value) {
		this.phase_id = value;
	}	    	
	public java.lang.String getPhase_name() {
		return this.phase_name;
	}

	public void setPhase_name(java.lang.String value) {
		this.phase_name = value;
	}	    	
	public java.lang.String getVm_task_name() {
		return this.vm_task_name;
	}

	public void setVm_task_name(java.lang.String value) {
		this.vm_task_name = value;
	}	    	
	public java.lang.String getVm_task_no() {
		return this.vm_task_no;
	}

	public void setVm_task_no(java.lang.String value) {
		this.vm_task_no = value;
	}	    	
	public java.lang.Long getLink_id_begin() {
		return this.link_id_begin;
	}
	
	public void setLink_id_begin(java.lang.Long value) {
		this.link_id_begin = value;
	}
	
	public java.lang.Long getLink_id_end() {
		return this.link_id_end;
	}
	
	public void setLink_id_end(java.lang.Long value) {
		this.link_id_end = value;
	}
	public java.lang.String getLink_no() {
		return this.link_no;
	}

	public void setLink_no(java.lang.String value) {
		this.link_no = value;
	}	    	
	public java.lang.String getLink_no_type() {
		return this.link_no_type;
	}

	public void setLink_no_type(java.lang.String value) {
		this.link_no_type = value;
	}	    	
	public java.lang.String getRole_code() {
		return this.role_code;
	}

	public void setRole_code(java.lang.String value) {
		this.role_code = value;
	}	    	
	public java.lang.String getLink_url() {
		return this.link_url;
	}

	public void setLink_url(java.lang.String value) {
		this.link_url = value;
	}	    	
	public java.lang.Boolean getIs_print() {
		return this.is_print;
	}

	public void setIs_print(java.lang.Boolean value) {
		this.is_print = value;
	}	    	
	public java.lang.String getCan_back() {
		return this.can_back;
	}

	public void setCan_back(java.lang.String value) {
		this.can_back = value;
	}	    	
	public java.lang.String getBack_btn() {
		return this.back_btn;
	}

	public void setBack_btn(java.lang.String value) {
		this.back_btn = value;
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

