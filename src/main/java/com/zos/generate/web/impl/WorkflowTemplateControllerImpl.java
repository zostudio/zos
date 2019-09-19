package com.zos.generate.web.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zos.generate.bo.WorkflowTemplateBO;
import com.zos.generate.common.ExecuteResultDTO;
import com.zos.generate.common.Page;
import com.zos.generate.dto.WorkflowTemplateDTO;
import com.zos.generate.dto.WorkflowTemplateQueryPageDTO;
import com.zos.generate.mapper.WorkflowTemplateMapper;
import com.zos.generate.service.IWorkflowTemplateService;
import com.zos.generate.web.IWorkflowTemplateController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author joker
 */
@Data
@Slf4j
@Transactional
@RestController
@AllArgsConstructor
@RequiredArgsConstructor
@Api(value = "增删改查操作接口(流程无关)")
public class WorkflowTemplateControllerImpl implements IWorkflowTemplateController {
	
	@Autowired
	private IWorkflowTemplateService workflowTemplateService;
	
	@Override
	@ApiOperation(value = "新增(流程无关)")
	public ExecuteResultDTO save(@RequestBody WorkflowTemplateDTO workflowTemplateDTO) throws Exception {
		ExecuteResultDTO result = new ExecuteResultDTO();
		try {
			workflowTemplateDTO = WorkflowTemplateMapper.INSTANCE.boToDto(workflowTemplateService.save(WorkflowTemplateMapper.INSTANCE.dtoToBo(workflowTemplateDTO)));
			result.setCode(1);
			result.setMsg("success");
			Map<String, Long> primaryKey = new HashMap<String, Long>();
			primaryKey.put("template_id", workflowTemplateDTO.getTemplateId());
			result.setObj(primaryKey);
		} catch (Exception e) {
			result.setCode(0);
			result.setMsg(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
			log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
		return result;
	}
	
	@Override
	@ApiOperation(value = "依据主键查询(流程无关)")
	public ExecuteResultDTO findByPrimaryKey(@PathVariable java.lang.Long templateId) throws Exception {
		ExecuteResultDTO result = new ExecuteResultDTO();
		try {
			WorkflowTemplateDTO workflowTemplateDTO = WorkflowTemplateMapper.INSTANCE.boToDto(workflowTemplateService.findByPrimaryKey(templateId));
			result.setCode(1);
			result.setMsg("success");
			result.setObj(workflowTemplateDTO);
		} catch (Exception e) {
			result.setCode(0);
			result.setMsg(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
			log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
		return result;
	}
	
	@Override
	@ApiOperation(value = "更新(流程无关)")
	public ExecuteResultDTO update(@RequestBody WorkflowTemplateDTO workflowTemplateDTO) throws Exception {
		ExecuteResultDTO result = new ExecuteResultDTO();
		try {
			workflowTemplateDTO = WorkflowTemplateMapper.INSTANCE.boToDto(workflowTemplateService.update(WorkflowTemplateMapper.INSTANCE.dtoToBo(workflowTemplateDTO)));
			result.setCode(1);
			result.setMsg("success");
			Map<String, Long> primaryKey = new HashMap<String, Long>();
			primaryKey.put("template_id", workflowTemplateDTO.getTemplateId());
			result.setObj(primaryKey);
		} catch (Exception e) {
			result.setCode(0);
			result.setMsg(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
			log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
		return result;
	}
	
	@Override
	@ApiOperation(value = "分页查询(流程无关)")
	public ExecuteResultDTO findByConds(WorkflowTemplateQueryPageDTO workflowTemplateQueryPageDTO) throws Exception {
		ExecuteResultDTO result = new ExecuteResultDTO();
		try {
			Page<WorkflowTemplateBO> workflowTemplateBOs = workflowTemplateService.findByConds(WorkflowTemplateMapper.INSTANCE.dtoToBo(workflowTemplateQueryPageDTO));
			result.setCode(1);
			result.setMsg("success");
			result.setObj(WorkflowTemplateMapper.INSTANCE.boToDto(workflowTemplateBOs));
		} catch (Exception e) {
			result.setCode(0);
			result.setMsg(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
			log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
		return result;
	}
	
}