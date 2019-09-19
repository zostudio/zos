package com.zos.generate.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zos.generate.bo.WorkflowBO;
import com.zos.generate.bo.WorkflowQueryPageBO;
import com.zos.generate.common.Page;
import com.zos.generate.dao.CommonDAO;
import com.zos.generate.domain.BOWorkflow;
import com.zos.generate.mapper.WorkflowMapper;
import com.zos.generate.service.IWorkflowService;

/**
 * 流程模版
 * 
 * @author joker
 */
@Service("workflowService")
@Transactional(rollbackOn = Exception.class)
public class WorkflowServiceImpl implements IWorkflowService {
	
	@Autowired
	private CommonDAO commonDAO;
	
	@Override
	public WorkflowBO save(WorkflowBO workflowBO) throws Exception {
		BOWorkflow boWorkflow = WorkflowMapper.INSTANCE.boToDomain(workflowBO);
		boWorkflow = commonDAO.saveOrUpdate(boWorkflow, BOWorkflow.class);
		return WorkflowMapper.INSTANCE.domainToBo(boWorkflow);
	}
	
	@Override
	public WorkflowBO findByPrimaryKey(java.lang.Long wfItemId) throws Exception {
		BOWorkflow boWorkflow = commonDAO.findById(BOWorkflow.class, wfItemId);
		return WorkflowMapper.INSTANCE.domainToBo(boWorkflow);
	}
	
	@Override
	public WorkflowBO update(WorkflowBO workflowBO) throws Exception {
		BOWorkflow boWorkflow = WorkflowMapper.INSTANCE.boToDomain(workflowBO);
		boWorkflow = commonDAO.saveOrUpdate(boWorkflow, BOWorkflow.class);
		return WorkflowMapper.INSTANCE.domainToBo(boWorkflow);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Page<WorkflowBO> findByConds(WorkflowQueryPageBO workflowQueryPageBO) throws Exception {
		StringBuffer hql = new StringBuffer("SELECT o FROM BOWorkflow o WHERE 1 = 1");
		Map<String, Object> param = new HashMap<String, Object>();
	    if (workflowQueryPageBO.getWfItemIdBegin() != null) {
			hql.append(" AND o.wfItemId >= :wfItemIdBegin");
			param.put("wfItemIdBegin", workflowQueryPageBO.getWfItemIdBegin());
		}
	    if (workflowQueryPageBO.getWfItemIdEnd() != null) {
			hql.append(" AND o.wfItemId <= :wfItemIdEnd");
			param.put("wfItemIdEnd", workflowQueryPageBO.getWfItemIdEnd());
		}
	    if (!StringUtils.isBlank(workflowQueryPageBO.getProcessKey())) {
			hql.append(" AND o.processKey LIKE :processKey");
			param.put("processKey", "%"+workflowQueryPageBO.getProcessKey()+"%");
		}
	    if (!StringUtils.isBlank(workflowQueryPageBO.getPhaseId())) {
			hql.append(" AND o.phaseId LIKE :phaseId");
			param.put("phaseId", "%"+workflowQueryPageBO.getPhaseId()+"%");
		}
	    if (!StringUtils.isBlank(workflowQueryPageBO.getPhaseName())) {
			hql.append(" AND o.phaseName LIKE :phaseName");
			param.put("phaseName", "%"+workflowQueryPageBO.getPhaseName()+"%");
		}
	    if (!StringUtils.isBlank(workflowQueryPageBO.getVmTaskName())) {
			hql.append(" AND o.vmTaskName LIKE :vmTaskName");
			param.put("vmTaskName", "%"+workflowQueryPageBO.getVmTaskName()+"%");
		}
	    if (!StringUtils.isBlank(workflowQueryPageBO.getVmTaskNo())) {
			hql.append(" AND o.vmTaskNo LIKE :vmTaskNo");
			param.put("vmTaskNo", "%"+workflowQueryPageBO.getVmTaskNo()+"%");
		}
	    if (workflowQueryPageBO.getLinkIdBegin() != null) {
			hql.append(" AND o.linkId >= :linkIdBegin");
			param.put("linkIdBegin", workflowQueryPageBO.getLinkIdBegin());
		}
	    if (workflowQueryPageBO.getLinkIdEnd() != null) {
			hql.append(" AND o.linkId <= :linkIdEnd");
			param.put("linkIdEnd", workflowQueryPageBO.getLinkIdEnd());
		}
	    if (!StringUtils.isBlank(workflowQueryPageBO.getLinkNo())) {
			hql.append(" AND o.linkNo LIKE :linkNo");
			param.put("linkNo", "%"+workflowQueryPageBO.getLinkNo()+"%");
		}
	    if (!StringUtils.isBlank(workflowQueryPageBO.getLinkNoType())) {
			hql.append(" AND o.linkNoType LIKE :linkNoType");
			param.put("linkNoType", "%"+workflowQueryPageBO.getLinkNoType()+"%");
		}
	    if (!StringUtils.isBlank(workflowQueryPageBO.getRoleCode())) {
			hql.append(" AND o.roleCode LIKE :roleCode");
			param.put("roleCode", "%"+workflowQueryPageBO.getRoleCode()+"%");
		}
	    if (!StringUtils.isBlank(workflowQueryPageBO.getLinkUrl())) {
			hql.append(" AND o.linkUrl LIKE :linkUrl");
			param.put("linkUrl", "%"+workflowQueryPageBO.getLinkUrl()+"%");
		}
	    if (workflowQueryPageBO.getIsPrint() != null) {
			hql.append(" AND o.isPrint = :isPrint");
			param.put("isPrint", workflowQueryPageBO.getIsPrint());
		}
	    if (!StringUtils.isBlank(workflowQueryPageBO.getCanBack())) {
			hql.append(" AND o.canBack LIKE :canBack");
			param.put("canBack", "%"+workflowQueryPageBO.getCanBack()+"%");
		}
	    if (!StringUtils.isBlank(workflowQueryPageBO.getBackBtn())) {
			hql.append(" AND o.backBtn LIKE :backBtn");
			param.put("backBtn", "%"+workflowQueryPageBO.getBackBtn()+"%");
		}
		hql.append(" ORDER BY o.wfItemId DESC");
		List<BOWorkflow> boWorkflows = commonDAO.findByJPAQL(hql.toString(), param);
		Page<WorkflowBO> page = new Page<WorkflowBO>();
		if ((workflowQueryPageBO.getPageNo() != null && workflowQueryPageBO.getPageNo().compareTo(0) > 0) && (workflowQueryPageBO.getPageSize() != null && workflowQueryPageBO.getPageSize().compareTo(0) > 0)) {
			if (boWorkflows.isEmpty()) {
				return page;
			}
			page.setRowCount(boWorkflows.size());
			boWorkflows = commonDAO.findByJPAQL(hql.toString(), param, workflowQueryPageBO.getPageNo(), workflowQueryPageBO.getPageSize());
			page.generatePageCount(workflowQueryPageBO.getPageSize());
		}
		page.setPageData(WorkflowMapper.INSTANCE.domainToBo(boWorkflows));
		return page;
	}
	
}