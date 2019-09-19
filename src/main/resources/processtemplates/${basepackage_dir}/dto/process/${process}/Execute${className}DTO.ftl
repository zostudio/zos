package ${basepackage}.dto.process.${process};

import ${basepackage}.dto.process.common.ExecuteProcessDTO;
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
public class Execute${className}DTO {
	<#list processGM.tables as table>
	
	@ApiModelProperty(value = "${table.remarks?default("")}")
	private ${table.className}DTO ${table.className?uncap_first}DTO;
	</#list>
	
	@ApiModelProperty(value = "流程启动信息")
	private ExecuteProcessDTO executeProcessDTO;
}