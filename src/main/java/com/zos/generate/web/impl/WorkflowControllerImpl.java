package com.zos.generate.web.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zos.generate.bo.WorkflowBO;
import com.zos.generate.common.ExecuteResultDTO;
import com.zos.generate.common.Page;
import com.zos.generate.dto.WorkflowDTO;
import com.zos.generate.dto.WorkflowQueryPageDTO;
import com.zos.generate.mapper.WorkflowMapper;
import com.zos.generate.service.IWorkflowService;
import com.zos.generate.web.IWorkflowController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 流程模版
 * 
 * @author joker
 */
@Data
@Slf4j
@Transactional
@RestController
@AllArgsConstructor
@RequiredArgsConstructor
@Api(value = "流程模版增删改查操作接口(流程无关)")
public class WorkflowControllerImpl implements IWorkflowController {
	
	@Autowired
	private IWorkflowService workflowService;
	
	@Override
	@ApiOperation(value = "新增流程模版(流程无关)")
	public ExecuteResultDTO save(@RequestBody WorkflowDTO workflowDTO) throws Exception {
		ExecuteResultDTO result = new ExecuteResultDTO();
		try {
			workflowDTO = WorkflowMapper.INSTANCE.boToDto(workflowService.save(WorkflowMapper.INSTANCE.dtoToBo(workflowDTO)));
			result.setCode(1);
			result.setMsg("success");
			Map<String, Long> primaryKey = new HashMap<String, Long>();
			primaryKey.put("wf_item_id", workflowDTO.getWfItemId());
			result.setObj(primaryKey);
		} catch (Exception e) {
			result.setCode(0);
			result.setMsg(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
			log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
		return result;
	}
	
	@Override
	@ApiOperation(value = "依据主键查询流程模版(流程无关)")
	public ExecuteResultDTO findByPrimaryKey(@PathVariable java.lang.Long wfItemId) throws Exception {
		ExecuteResultDTO result = new ExecuteResultDTO();
		try {
			WorkflowDTO workflowDTO = WorkflowMapper.INSTANCE.boToDto(workflowService.findByPrimaryKey(wfItemId));
			result.setCode(1);
			result.setMsg("success");
			result.setObj(workflowDTO);
		} catch (Exception e) {
			result.setCode(0);
			result.setMsg(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
			log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
		return result;
	}
	
	@Override
	@ApiOperation(value = "更新流程模版(流程无关)")
	public ExecuteResultDTO update(@RequestBody WorkflowDTO workflowDTO) throws Exception {
		ExecuteResultDTO result = new ExecuteResultDTO();
		try {
			workflowDTO = WorkflowMapper.INSTANCE.boToDto(workflowService.update(WorkflowMapper.INSTANCE.dtoToBo(workflowDTO)));
			result.setCode(1);
			result.setMsg("success");
			Map<String, Long> primaryKey = new HashMap<String, Long>();
			primaryKey.put("wf_item_id", workflowDTO.getWfItemId());
			result.setObj(primaryKey);
		} catch (Exception e) {
			result.setCode(0);
			result.setMsg(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
			log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
		return result;
	}
	
	@Override
	@ApiOperation(value = "分页查询流程模版(流程无关)")
	public ExecuteResultDTO findByConds(WorkflowQueryPageDTO workflowQueryPageDTO) throws Exception {
		ExecuteResultDTO result = new ExecuteResultDTO();
		try {
			Page<WorkflowBO> workflowBOs = workflowService.findByConds(WorkflowMapper.INSTANCE.dtoToBo(workflowQueryPageDTO));
			result.setCode(1);
			result.setMsg("success");
			result.setObj(WorkflowMapper.INSTANCE.boToDto(workflowBOs));
		} catch (Exception e) {
			result.setCode(0);
			result.setMsg(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
			log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
		return result;
	}
	
}