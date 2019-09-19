package com.zos.generate.service;

import com.zos.generate.bo.WorkflowTemplateBO;
import com.zos.generate.bo.WorkflowTemplateQueryPageBO;
import com.zos.generate.common.Page;

/**
 * 
 * 
 * @author joker
 */
public interface IWorkflowTemplateService {
	
	public WorkflowTemplateBO save(WorkflowTemplateBO workflowTemplateBO) throws Exception;
	
	public WorkflowTemplateBO findByPrimaryKey(java.lang.Long templateId) throws Exception;
	
	public WorkflowTemplateBO update(WorkflowTemplateBO workflowTemplateBO) throws Exception;
	
	public Page<WorkflowTemplateBO> findByConds(WorkflowTemplateQueryPageBO workflowTemplateQueryPageBO) throws Exception;
	
}