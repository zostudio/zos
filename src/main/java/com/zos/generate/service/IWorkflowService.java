package com.zos.generate.service;

import com.zos.generate.bo.WorkflowBO;
import com.zos.generate.bo.WorkflowQueryPageBO;
import com.zos.generate.common.Page;

/**
 * 流程模版
 * 
 * @author joker
 */
public interface IWorkflowService {
	
	public WorkflowBO save(WorkflowBO workflowBO) throws Exception;
	
	public WorkflowBO findByPrimaryKey(java.lang.Long wfItemId) throws Exception;
	
	public WorkflowBO update(WorkflowBO workflowBO) throws Exception;
	
	public Page<WorkflowBO> findByConds(WorkflowQueryPageBO workflowQueryPageBO) throws Exception;
	
}