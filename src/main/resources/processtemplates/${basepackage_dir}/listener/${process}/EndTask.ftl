package ${basepackage}.listener.${process};

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.asiainfo.rms.workflow.bo.workflow.WorkflowBO;
import com.asiainfo.rms.workflow.service.workflow.IAlmWorkflowSV;
import com.asiainfo.rms.workflow.dao.CommonDAO;
import com.asiainfo.rms.workflow.listener.baseLinstener.EndTaskListener;

import lombok.extern.log4j.Log4j;

/**
 * ${processGM.processName}
 * 
 * @author 01Studio
 */
@Log4j
@Transactional(rollbackOn=Exception.class)
@Component("${processGM.endListener.implementation}")
public class EndTask extends EndTaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Resource(name="workflowSV")
	private IAlmWorkflowSV workflowSV;
	
	@Resource(name="commonDao")
	private CommonDAO dao;
	
	@Override
	public void updateObjCurPhaseAndCurStatus(String objId, String objType, String linkId) throws Exception {
		Map<Integer, Object> param = new HashMap<Integer, Object>();
		param.put(1, new Integer(linkId));
		WorkflowBO workflowBO = workflowSV.getWorkflowByLinkId(Long.valueOf(linkId));
		param.put(2, new Integer(workflowBO.getPhaseId()));
		param.put(3, Long.valueOf(objId));
		String hql = "UPDATE <@generateTableName/> SET CUR_STATUS=?, CUR_PHASE=? WHERE <@generateObjId/>=?";
		log.info("UPDATE <@generateTableName/> SET CUR_STATUS="+linkId+", CUR_PHASE="+workflowBO.getPhaseId()+" WHERE <@generateObjId/>="+objId);
		int count = dao.updateBySql(hql, param);
		log.info("更新条目: " + count);
	}

}
<#macro generateTableName><#list processGM.tables as table><#if table_index == 0>${table.sqlName?upper_case}</#if></#list></#macro>
<#macro generateObjId><#list processGM.tables as table><#if table_index == 0><#list table.columns as column><#if column.pk>${column.sqlName?upper_case}</#if><#if !column.pk><#if table.pkCount==0 && column_index==0>${column.sqlName?upper_case}</#if></#if></#list></#if></#list></#macro>