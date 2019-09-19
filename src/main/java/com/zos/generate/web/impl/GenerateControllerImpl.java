package com.zos.generate.web.impl;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zos.generate.dto.ProcessDiagrameDTO;
import com.zos.generate.dto.ProcessGenerateDTO;
import com.zos.generate.excel.GenerateExport;
import com.zos.generate.excel.WorkflowExcel;
import com.zos.generate.excel.WorkflowTemplateExcel;
import com.zos.generate.mapper.GenerateMapper;
import com.zos.generate.mapper.WorkflowMapper;
import com.zos.generate.mapper.WorkflowTemplateMapper;
import com.zos.generate.service.IGenerateService;
import com.zos.generate.web.IGererateController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@RestController
@RequestMapping("/generate")
@Api(value = "自动生成流程接口代码")
public class GenerateControllerImpl implements IGererateController {
	
	@Autowired
	private IGenerateService generateService;

	@Override
	@ApiOperation(value = "第二步流程配置入库后生成流程接口")
	public String process(@RequestBody ProcessGenerateDTO processGenerateDTO) throws Exception {
		log.info("process");
		return generateService.generateRest(processGenerateDTO.getFileName(), processGenerateDTO.getTableNames());
	}

	@Override
	@ApiOperation(value = "增删改查")
	public String crud(@RequestBody List<String> tableNames) {
		log.info("crud");
		generateService.generateCrud(tableNames);
		return null;
	}

	@Override
	@ApiOperation(value = "第一步通过流程图分析流程模版模型,fileName流程文件名称,startId是wfItemId和linkId共同的期望起始值")
	public ProcessDiagrameDTO workflow(@PathVariable String fileName, @PathVariable Long startId) throws Exception {
		ProcessDiagrameDTO processDiagrameDTO = new ProcessDiagrameDTO();
		processDiagrameDTO.setWorkflowDTOs(WorkflowMapper.INSTANCE.boToDto(generateService.workflow(fileName, startId)));
		if (CollectionUtils.isEmpty(processDiagrameDTO.getWorkflowDTOs())) {
			log.error("流程配置为空");
		} else {
			processDiagrameDTO.setWorkflowTemplateDTO(WorkflowTemplateMapper.INSTANCE.boToDto(generateService.template(fileName)));
		}
		return processDiagrameDTO;
	}

	@Override
	@ApiOperation(value = "第一步通过流程图分析流程模版模型(Excel),fileName流程文件名称,startId是wfItemId和linkId共同的期望起始值")
	public void workflow(@PathVariable String fileName, @PathVariable Long startId, HttpServletResponse response)
			throws Exception {
		List<WorkflowExcel> workflowExcels = GenerateMapper.INSTANCE.boToExcel(generateService.workflow(fileName, startId));
		if (CollectionUtils.isEmpty(workflowExcels)) {
			log.error("流程配置为空");
		} else {
			Long index = 0L;
			StringBuffer insertSql = new StringBuffer();
			for (WorkflowExcel workflowExcel : workflowExcels) {
				workflowExcel.setSequence(index++);
				insertSql.append(workflowExcel.getInsertSql());
				insertSql.append("\n");
			}
			log.info(insertSql.toString());
			WorkflowTemplateExcel workflowTemplateExcel = GenerateMapper.INSTANCE.boToExcel(generateService.template(fileName));
			workflowTemplateExcel.setSequence(0L);
			workflowTemplateExcel.logInsertSql();
			GenerateExport.exportExcel(workflowExcels, workflowTemplateExcel, response);
		}
	}

	@Override
	@ApiOperation(value = "流程信息")
	public BpmnModel processInfo(@PathVariable String fileName) throws Exception {
		return generateService.processInfo(fileName);
	}

}
