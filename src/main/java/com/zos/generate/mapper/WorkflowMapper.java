package com.zos.generate.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.zos.generate.bo.WorkflowBO;
import com.zos.generate.bo.WorkflowQueryPageBO;
import com.zos.generate.common.Page;
import com.zos.generate.common.WorkflowBOEnhance;
import com.zos.generate.domain.BOWorkflow;
import com.zos.generate.dto.WorkflowDTO;
import com.zos.generate.dto.WorkflowQueryPageDTO;

/**
 * 流程模版
 * 
 * @author joker
 */
@Mapper
public interface WorkflowMapper {
	
	WorkflowMapper INSTANCE = Mappers.getMapper(WorkflowMapper.class);

	public WorkflowDTO boToDto(WorkflowBO workflowBO);
	
	public List<WorkflowDTO> boToDto(List<WorkflowBO> workflowBOs);
	
	public Page<WorkflowDTO> boToDto(Page<WorkflowBO> workflowBOs);

	@Mappings({
		@Mapping(source = "wf_item_id_begin", target = "wfItemIdBegin"),
		@Mapping(source = "wf_item_id_end", target = "wfItemIdEnd"),
		@Mapping(source = "process_key", target = "processKey"),
		@Mapping(source = "phase_id", target = "phaseId"),
		@Mapping(source = "phase_name", target = "phaseName"),
		@Mapping(source = "vm_task_name", target = "vmTaskName"),
		@Mapping(source = "vm_task_no", target = "vmTaskNo"),
		@Mapping(source = "link_id_begin", target = "linkIdBegin"),
		@Mapping(source = "link_id_end", target = "linkIdEnd"),
		@Mapping(source = "link_no", target = "linkNo"),
		@Mapping(source = "link_no_type", target = "linkNoType"),
		@Mapping(source = "role_code", target = "roleCode"),
		@Mapping(source = "link_url", target = "linkUrl"),
		@Mapping(source = "is_print", target = "isPrint"),
		@Mapping(source = "can_back", target = "canBack"),
		@Mapping(source = "back_btn", target = "backBtn"),
        @Mapping(source = "page_no", target = "pageNo"),
        @Mapping(source = "page_size", target = "pageSize")
    })
	public WorkflowQueryPageBO dtoToBo(WorkflowQueryPageDTO workflowQueryPageDTO);
	
	public WorkflowBO dtoToBo(WorkflowDTO workflowDTO);

	public BOWorkflow boToDomain(WorkflowBO workflowBO);

	public WorkflowBO domainToBo(BOWorkflow boWorkflow);
	
	public List<WorkflowBO> domainToBo(List<BOWorkflow> boWorkflows);
	
	public Page<WorkflowBO> domainToBo(Page<BOWorkflow> boWorkflows);
	
	@Mappings({
		@Mapping(target = "outGoingFlows", ignore = true)
    })
	public WorkflowBOEnhance botoEnhance(WorkflowBO workflowBO);
}