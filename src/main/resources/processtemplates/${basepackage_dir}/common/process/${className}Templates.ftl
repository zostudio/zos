package ${basepackage}.common.process;

/**
 * ${processGM.processName}
 *
 * @author 01Studio
 */
public class ${className}Templates {
	<#list processGM.phases?keys as key>
	
	/**
	 * 流程: ${processGM.processName}</br>
	 * 阶段: ${processGM.phases["${key}"]}</br>
	 */
	public final static Integer PHASE_${key}_ID = ${key};
	</#list>
	<#list processGM.workflows as workflow>
	
	/**
	 * 流程: ${processGM.processName}</br>
	 * 阶段: ${workflow.phaseName}</br>
	 * 环节: ${workflow.vmTaskName}</br>
	 */
	public final static Integer LINK_ID_${workflow.linkId} = ${workflow.linkId};
	
	/**
	 * 流程: ${processGM.processName}</br>
	 * 阶段: ${workflow.phaseName}</br>
	 * 环节: ${workflow.vmTaskName}</br>
	 */
	public final static String LINK_NO_${workflow.linkId} = "${workflow.linkNo}";
	</#list>
	
}