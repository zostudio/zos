package ${basepackage}.web.process.${process}.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ${basepackage}.dto.common.ExecuteResultDTO;
<#list processGM.tables as table>
import ${basepackage}.bo.process.${process}.${table.className}BO;
import ${basepackage}.mapper.process.${process}.${table.className}Mapper;
import ${basepackage}.service.process.${process}.I${table.className}Service;
</#list>
import ${basepackage}.dto.process.${process}.Launch${className}DTO;
import ${basepackage}.dto.process.${process}.Execute${className}DTO;
import com.asiainfo.rms.workflow.dto.process.common.WorkOrderStakeholderDTO;
import ${basepackage}.web.process.${process}.I${className}Controller;
import com.asiainfo.rms.workflow.mapper.workflow.StakeholderMapper;
import com.asiainfo.rms.workflow.mapper.workflow.WorkorderMapper;
import com.asiainfo.rms.workflow.bo.process.common.LaunchProcessBO;
import com.asiainfo.rms.workflow.bo.process.common.ExecuteProcessBO;
import com.asiainfo.rms.workflow.util.ProcessUtil;
import com.asiainfo.rms.workflow.common.constance.ConstDefine;
import com.asiainfo.rms.workflow.common.constance.${className}ConstDefine;
import com.asiainfo.rms.workflow.service.api.WorkflowControllerApi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

/**
 * ${processGM.processName}
 * 
 * @author 01Studio
 */
@Data
@Log4j
@Transactional
@RestController
@AllArgsConstructor
@RequiredArgsConstructor
@Api(tags={ "${processGM.processName}[<#list processGM.allEndEvent as endEvent><#if endEvent_index != 0>,</#if>{${endEvent.phaseId}:${endEvent.phaseName},${endEvent.linkId}:${endEvent.linkNo}(${endEvent.vmTaskName})}</#list>]"}, value="${processGM.processName}")
public class ${className}ControllerImpl implements I${className}Controller {

	@Autowired
	private WorkflowControllerApi api;
	<@generateServices/>
	
	@Override
	@ApiOperation(value="${processGM.processName}新建工单[新建]", tags={"[<#list processGM.allStartEvent as startEvent><#if startEvent_index != 0>,</#if>{${startEvent.phaseId}:${startEvent.phaseName},${startEvent.linkId}:${startEvent.linkNo}(${startEvent.vmTaskName})}</#list>]"})
	public ExecuteResultDTO saveFirstOrder(@RequestBody Launch${className}DTO launch${className}DTO) throws Exception {
		log.info("[<#list processGM.allStartEvent as startEvent><#if startEvent_index != 0>,</#if>{${startEvent.phaseId}:${startEvent.phaseName},${startEvent.linkId}:${startEvent.linkNo}(${startEvent.vmTaskName})}</#list>]");
		log.info("${processGM.processName}新建工单[新建]: " + launch${className}DTO.toString());
		ExecuteResultDTO executeResult = new ExecuteResultDTO();
		<@generateLaunchTables/>
		LaunchProcessBO launchProcessBO = ProcessUtil.assemblyLaunchProcess(launch${className}DTO.getLaunchProcessDTO(),
			<@generateProcessObjId/>, ${className}ConstDefine.TEMPLATE_PATH,
			${className}ConstDefine.TEMPLATE_KEY, ${className}ConstDefine.OBJ_TYPE,
			ConstDefine.STDHOLDE_TYPE_APPROVAL);
		api.saveFirstOrder(launchProcessBO);
		executeResult.setCode(1);
		executeResult.setMsg("success");
		return executeResult;
	}
	
	@Override
	@ApiOperation(value="${processGM.processName}启动[新建和启动]", tags={"[<#list processGM.allStartEvent as startEvent><#if startEvent_index != 0>,</#if>{${startEvent.phaseId}:${startEvent.phaseName},${startEvent.linkId}:${startEvent.linkNo}(${startEvent.vmTaskName})}</#list>,<#list processGM.allStartEvent as startEvent><#if startEvent_index != 0>,</#if>{<#list startEvent.outGoingFlows as outGoingFlow><#if outGoingFlow_index != 0>,</#if><#if outGoingFlow.conditionExpression?default("")?trim?length gt 1>${outGoingFlow.conditionExpression}<#else>P</#if>:[<#list outGoingFlow.workflowBOs as workflowBO><#if workflowBO_index != 0>,</#if>{link_id:${workflowBO.linkId},link_no:${workflowBO.linkNo},name:${workflowBO.vmTaskName}}</#list>]}</#list></#list>]"})
	public ExecuteResultDTO launchProcess(@RequestBody Launch${className}DTO launch${className}DTO) throws Exception {
		log.info("[<#list processGM.allStartEvent as startEvent><#if startEvent_index != 0>,</#if>{${startEvent.phaseId}:${startEvent.phaseName},${startEvent.linkId}:${startEvent.linkNo}(${startEvent.vmTaskName})}</#list>,<#list processGM.allStartEvent as startEvent><#if startEvent_index != 0>,</#if>{<#list startEvent.outGoingFlows as outGoingFlow><#if outGoingFlow_index != 0>,</#if><#if outGoingFlow.conditionExpression?default("")?trim?length gt 1>${outGoingFlow.conditionExpression}<#else>P</#if>:[<#list outGoingFlow.workflowBOs as workflowBO><#if workflowBO_index != 0>,</#if>{link_id:${workflowBO.linkId},link_no:${workflowBO.linkNo},name:${workflowBO.vmTaskName}}</#list>]}</#list></#list>]");
		log.info("${processGM.processName}启动[新建和启动]: " + launch${className}DTO.toString());
		ExecuteResultDTO executeResult = new ExecuteResultDTO();
		<@generateLaunchTables/>
		LaunchProcessBO launchProcessBO = ProcessUtil.assemblyLaunchProcess(launch${className}DTO.getLaunchProcessDTO(),
			<@generateProcessObjId/>, ${className}ConstDefine.TEMPLATE_PATH,
			${className}ConstDefine.TEMPLATE_KEY, ${className}ConstDefine.OBJ_TYPE,
			ConstDefine.STDHOLDE_TYPE_APPROVAL);
		api.createWorkflow(launchProcessBO);
		executeResult.setCode(1);
		executeResult.setMsg("success");
		return executeResult;
	}

	@Override
	@ApiOperation(value="${processGM.processName}启动[保存工单和处理人]", tags={"[<#list processGM.allStartEvent as startEvent><#if startEvent_index != 0>,</#if>{${startEvent.phaseId}:${startEvent.phaseName},${startEvent.linkId}:${startEvent.linkNo}(${startEvent.vmTaskName})}</#list>,<#list processGM.allStartEvent as startEvent><#if startEvent_index != 0>,</#if>{<#list startEvent.outGoingFlows as outGoingFlow><#if outGoingFlow_index != 0>,</#if><#if outGoingFlow.conditionExpression?default("")?trim?length gt 1>${outGoingFlow.conditionExpression}<#else>P</#if>:[<#list outGoingFlow.workflowBOs as workflowBO><#if workflowBO_index != 0>,</#if>{link_id:${workflowBO.linkId},link_no:${workflowBO.linkNo},name:${workflowBO.vmTaskName}}</#list>]}</#list></#list>]"})
	public ExecuteResultDTO wsLaunchProcess(@RequestBody WorkOrderStakeholderDTO workOrderStakeholderDTO) throws Exception {
		log.info("[<#list processGM.allStartEvent as startEvent><#if startEvent_index != 0>,</#if>{${startEvent.phaseId}:${startEvent.phaseName},${startEvent.linkId}:${startEvent.linkNo}(${startEvent.vmTaskName})}</#list>,<#list processGM.allStartEvent as startEvent><#if startEvent_index != 0>,</#if>{<#list startEvent.outGoingFlows as outGoingFlow><#if outGoingFlow_index != 0>,</#if><#if outGoingFlow.conditionExpression?default("")?trim?length gt 1>${outGoingFlow.conditionExpression}<#else>P</#if>:[<#list outGoingFlow.workflowBOs as workflowBO><#if workflowBO_index != 0>,</#if>{link_id:${workflowBO.linkId},link_no:${workflowBO.linkNo},name:${workflowBO.vmTaskName}}</#list>]}</#list></#list>]");
		log.info("${processGM.processName}启动[保存工单和处理人]: " + workOrderStakeholderDTO.toString());
		<@generateOrderStakeholder/>
	}
	<#list processGM.allUserEnhance as enhance>
	
	@Override
	@ApiOperation(value="${enhance.vmTaskName}[回单]", tags={"[{${enhance.phaseId}:${enhance.phaseName},${enhance.linkId}:${enhance.linkNo}(${enhance.vmTaskName})},<#list enhance.outGoingFlows as outGoingFlow><#if outGoingFlow_index != 0>,</#if>{<#if outGoingFlow.conditionExpression?default("")?trim?length gt 1>${outGoingFlow.conditionExpression}<#else>P</#if>:[<#list outGoingFlow.workflowBOs as workflowBO><#if workflowBO_index != 0>,</#if>{link_id:${workflowBO.linkId},link_no:${workflowBO.linkNo},name:${workflowBO.vmTaskName}}</#list>]}</#list>]"})
	public ExecuteResultDTO ${enhance.linkNo?uncap_first}(@RequestBody Execute${className}DTO execute${className}DTO) throws Exception {
		log.info("[{${enhance.phaseId}:${enhance.phaseName},${enhance.linkId}:${enhance.linkNo}(${enhance.vmTaskName})},<#list enhance.outGoingFlows as outGoingFlow><#if outGoingFlow_index != 0>,</#if>{<#if outGoingFlow.conditionExpression?default("")?trim?length gt 1>${outGoingFlow.conditionExpression}<#else>P</#if>:[<#list outGoingFlow.workflowBOs as workflowBO><#if workflowBO_index != 0>,</#if>{link_id:${workflowBO.linkId},link_no:${workflowBO.linkNo},name:${workflowBO.vmTaskName}}</#list>]}</#list>]");
		log.info("${processGM.processName}.${enhance.vmTaskName}[回单]: " + execute${className}DTO.toString());
		ExecuteResultDTO executeResult = new ExecuteResultDTO();
		<@generateExecuteTables/>
		ExecuteProcessBO executeProcessBO = ProcessUtil.assemblyExecuteProcess(execute${className}DTO.getExecuteProcessDTO(),
			<@generateProcessObjId/>, ${className}ConstDefine.TEMPLATE_PATH,
			${className}ConstDefine.TEMPLATE_KEY, ${className}ConstDefine.OBJ_TYPE,
			ConstDefine.STDHOLDE_TYPE_APPROVAL);
		api.finishUserTask(executeProcessBO);
		executeResult.setCode(1);
		executeResult.setMsg("success");
		return executeResult;
	}
	
	@Override
	@ApiOperation(value="${enhance.vmTaskName}[保存工单和处理人]", tags={"[{${enhance.phaseId}:${enhance.phaseName},${enhance.linkId}:${enhance.linkNo}(${enhance.vmTaskName})},<#list enhance.outGoingFlows as outGoingFlow><#if outGoingFlow_index != 0>,</#if>{<#if outGoingFlow.conditionExpression?default("")?trim?length gt 1>${outGoingFlow.conditionExpression}<#else>P</#if>:[<#list outGoingFlow.workflowBOs as workflowBO><#if workflowBO_index != 0>,</#if>{link_id:${workflowBO.linkId},link_no:${workflowBO.linkNo},name:${workflowBO.vmTaskName}}</#list>]}</#list>]"})
	public ExecuteResultDTO ws${enhance.linkNo?cap_first}(@RequestBody WorkOrderStakeholderDTO workOrderStakeholderDTO) throws Exception {
		log.info("[{${enhance.phaseId}:${enhance.phaseName},${enhance.linkId}:${enhance.linkNo}(${enhance.vmTaskName})},<#list enhance.outGoingFlows as outGoingFlow><#if outGoingFlow_index != 0>,</#if>{<#if outGoingFlow.conditionExpression?default("")?trim?length gt 1>${outGoingFlow.conditionExpression}<#else>P</#if>:[<#list outGoingFlow.workflowBOs as workflowBO><#if workflowBO_index != 0>,</#if>{link_id:${workflowBO.linkId},link_no:${workflowBO.linkNo},name:${workflowBO.vmTaskName}}</#list>]}</#list>]");
		log.info("${processGM.processName}.${enhance.vmTaskName}[保存工单和处理人]: " + workOrderStakeholderDTO.toString());
		<@generateOrderStakeholder/>
	}
	</#list>
}

<#macro generateServices>
	<#list processGM.tables as table>
	
	@Autowired
	I${table.className}Service ${table.className?uncap_first}Service;
	</#list>
</#macro>

<#macro generateLaunchTables>
	<#list processGM.tables as table>
		${table.className}BO ${table.className?uncap_first}BO = ${table.className}Mapper.INSTANCE.dtoToBo(launch${className}DTO.get${table.className}DTO());
	</#list>
		<#list processGM.tables as table>
			<#if table_index !=0>
		${table.className?uncap_first}BO.set<@generateMainTablePrimaryName/>(<@generateMainTablePrimaryId/>);
			</#if>
		${table.className?uncap_first}BO = ${table.className?uncap_first}Service.save(${table.className?uncap_first}BO);
		</#list>
</#macro>

<#macro generateExecuteTables>
	<#list processGM.tables as table>
		${table.className}BO ${table.className?uncap_first}BO = ${table.className}Mapper.INSTANCE.dtoToBo(execute${className}DTO.get${table.className}DTO());
	</#list>
		<#list processGM.tables as table>
			<#if table_index !=0>
		${table.className?uncap_first}BO.set<@generateMainTablePrimaryName/>(<@generateMainTablePrimaryId/>);
			</#if>
		${table.className?uncap_first}BO = ${table.className?uncap_first}Service.save(${table.className?uncap_first}BO);
		</#list>
</#macro>

<#macro generateOrderStakeholder>
		ExecuteResultDTO executeResult = new ExecuteResultDTO();
		if (workOrderStakeholderDTO != null && workOrderStakeholderDTO.getWorkorderUpdate() != null) {
			api.updateWorkorder(WorkorderMapper.INSTANCE.dtoToBo(workOrderStakeholderDTO.getWorkorderUpdate()));
		}
		if (workOrderStakeholderDTO != null && workOrderStakeholderDTO.getOprStakeholder() != null) {
			api.oprStakeholder(workOrderStakeholderDTO.getOprStakeholder().getObjId(), ConstDefine.OBJ_TYPE_BAREQ,
				StakeholderMapper.INSTANCE.dtoToBo(workOrderStakeholderDTO.getOprStakeholder().getStakeholderOprs()));
		}
		executeResult.setCode(1);
		executeResult.setMsg("success");
		return executeResult;
</#macro>

<#macro generateProcessObjId><#list processGM.tables as table><#if table_index == 0><#list table.columns as column><#if column.pk>Long.valueOf(${table.className?uncap_first}BO.get${column.columnName}())</#if><#if !column.pk><#if table.pkCount==0 && column_index==0>Long.valueOf(${table.className?uncap_first}BO.get${column.columnName}())</#if></#if></#list></#if></#list></#macro>
<#macro generateMainTablePrimaryId><#list processGM.tables as table><#if table_index == 0><#list table.columns as column><#if column.pk>${table.className?uncap_first}BO.get${column.columnName}()</#if><#if !column.pk><#if table.pkCount==0 && column_index==0>${table.className?uncap_first}BO.get${column.columnName}()</#if></#if></#list></#if></#list></#macro>
<#macro generateMainTablePrimaryName><#list processGM.tables as table><#if table_index == 0><#list table.columns as column><#if column.pk>${column.columnName}</#if><#if !column.pk><#if table.pkCount==0 && column_index==0>${column.columnName}</#if></#if></#list></#if></#list></#macro>