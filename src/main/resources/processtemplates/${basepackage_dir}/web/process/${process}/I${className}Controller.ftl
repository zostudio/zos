package ${basepackage}.web.process.${process};

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ${basepackage}.dto.common.ExecuteResultDTO;
import ${basepackage}.dto.process.${process}.Launch${className}DTO;
import ${basepackage}.dto.process.${process}.Execute${className}DTO;
import com.asiainfo.rms.workflow.dto.process.common.WorkOrderStakeholderDTO;

/**
 * ${processGM.processName}
 * 
 * @author 01Studio
 */
@RequestMapping("/process/${process}/${className?lower_case}")
public interface I${className}Controller {

	@PostMapping(value = "/sfo")
	public ExecuteResultDTO saveFirstOrder(@RequestBody Launch${className}DTO launch${className}DTO) throws Exception;
	
	@PostMapping(value = "/create")
	public ExecuteResultDTO launchProcess(@RequestBody Launch${className}DTO launch${className}DTO) throws Exception;

	@PostMapping(value = "/ws/create")
	public ExecuteResultDTO wsLaunchProcess(@RequestBody WorkOrderStakeholderDTO workOrderStakeholder) throws Exception;
	<#list processGM.allUserEnhance as enhance>
	
	@PostMapping(value = "/${enhance.linkNo?lower_case}")
	public ExecuteResultDTO ${enhance.linkNo?uncap_first}(@RequestBody Execute${className}DTO execute${className}DTO) throws Exception;

	@PostMapping(value = "/ws/${enhance.linkNo?lower_case}")
	public ExecuteResultDTO ws${enhance.linkNo?cap_first}(@RequestBody WorkOrderStakeholderDTO workOrderStakeholderDTO) throws Exception;	
	</#list>
}