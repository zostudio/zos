package com.zos.generate.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zos.generate.dto.ProcessDiagrameDTO;
import com.zos.generate.dto.ProcessGenerateDTO;

@RestController
@RequestMapping("/generate")
public interface IGererateController {

	@GetMapping("/workflow/{fileName}/{startId:\\d+}")
	public ProcessDiagrameDTO workflow(@PathVariable String fileName, @PathVariable Long startId) throws Exception;

	@GetMapping("/workflow/excel/{fileName}/{startId:\\d+}")
	public void workflow(@PathVariable String fileName, @PathVariable Long startId, HttpServletResponse response) throws Exception;

	@PostMapping("/process")
	public String process(@RequestBody ProcessGenerateDTO processGenerateDTO) throws Exception;
	
	@PostMapping("/crud")
	public String crud(@RequestBody List<String> tableNames);
	
	@GetMapping("/process/info/{fileName}")
	public BpmnModel processInfo(@PathVariable String fileName) throws Exception;

}
