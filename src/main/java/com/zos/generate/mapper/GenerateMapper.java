package com.zos.generate.mapper;

import java.util.List;

import org.activiti.bpmn.model.FlowElement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.zos.generate.bo.WorkflowBO;
import com.zos.generate.bo.WorkflowTemplateBO;
import com.zos.generate.excel.WorkflowExcel;
import com.zos.generate.excel.WorkflowTemplateExcel;

@Mapper
public interface GenerateMapper {
	
	GenerateMapper INSTANCE = Mappers.getMapper(GenerateMapper.class);
	
	@Mappings({
		@Mapping(source = "id", target = "vmTaskNo"),
		@Mapping(source = "name", target = "vmTaskName"),
		@Mapping(target = "wfItemId", defaultValue="0", ignore=true),
		@Mapping(target = "processKey", defaultValue="", ignore=true),
		@Mapping(target = "phaseId", defaultValue="0", ignore=true),
		@Mapping(target = "phaseName", defaultValue="", ignore=true),
		@Mapping(target = "linkId", defaultValue="", ignore=true),
		@Mapping(target = "linkNo", defaultValue="vmTaskNo", ignore=true),
		@Mapping(target = "linkNoType", defaultValue="", ignore=true),
		@Mapping(target = "roleCode", defaultValue="", ignore=true),
		@Mapping(target = "linkUrl", defaultValue="", ignore=true),
		@Mapping(target = "isPrint", defaultValue="", ignore=true),
		@Mapping(target = "canBack", defaultValue="N", ignore=true),
		@Mapping(target = "backBtn", defaultValue="N", ignore=true)
    })
	public WorkflowBO flowElementToWorkflowBO(FlowElement flowElement);
	
	public List<WorkflowBO> flowElementsToWorkflowBOs(List<FlowElement> flowElements);
	
	@Mappings({
		@Mapping(target = "sequence", defaultValue="0", ignore = true)
    })
	public WorkflowExcel boToExcel(WorkflowBO workflowBO);
	
	public List<WorkflowExcel> boToExcel(List<WorkflowBO> workflowBOs);
	
	@Mappings({
		@Mapping(target = "sequence", defaultValue="0", ignore = true)
    })
	public WorkflowTemplateExcel boToExcel(WorkflowTemplateBO workflowTemplateBO);
}
