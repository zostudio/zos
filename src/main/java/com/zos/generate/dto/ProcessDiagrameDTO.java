package com.zos.generate.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程生成模型
 * 
 * @author 01Studio
 *
 */
@ApiModel(description = "流程生成模型")
public class ProcessDiagrameDTO {
	
	@ApiModelProperty(value = "流程配置")
	private List<WorkflowDTO> workflowDTOs;

	@ApiModelProperty(value = "流程模版")
	private WorkflowTemplateDTO workflowTemplateDTO;

	public List<WorkflowDTO> getWorkflowDTOs() {
		return workflowDTOs;
	}

	public void setWorkflowDTOs(List<WorkflowDTO> workflowDTOs) {
		this.workflowDTOs = workflowDTOs;
	}

	public WorkflowTemplateDTO getWorkflowTemplateDTO() {
		return workflowTemplateDTO;
	}

	public void setWorkflowTemplateDTO(WorkflowTemplateDTO workflowTemplateDTO) {
		this.workflowTemplateDTO = workflowTemplateDTO;
	}
}
