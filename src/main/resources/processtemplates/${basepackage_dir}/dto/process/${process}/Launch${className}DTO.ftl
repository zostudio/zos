package ${basepackage}.dto.process.${process};

import ${basepackage}.dto.process.common.LaunchProcessDTO;
<#list processGM.tables as table>
import ${basepackage}.dto.process.${process}.${table.className}DTO;
</#list>

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ${processGM.processName}
 * 
 * @author 01Studio
 */
@Data
@ApiModel(description = "${processGM.processName}")
public class Launch${className}DTO {
	<#list processGM.tables as table>
	
	@ApiModelProperty(value = "${table.remarks?default("暂无表注释")}", example = "${table.remarks?default("暂无表注释")}")
	private ${table.className}DTO ${table.className?uncap_first}DTO;
	</#list>
	
	@ApiModelProperty(value = "流程启动信息", example = "流程启动信息")
	private LaunchProcessDTO launchProcessDTO;
}