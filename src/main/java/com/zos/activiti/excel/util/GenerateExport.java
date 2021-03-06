package com.zos.activiti.excel.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;

public class GenerateExport {

	public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName,
			boolean isCreateHeader, HttpServletResponse response) throws Exception {
		ExportParams exportParams = new ExportParams(title, sheetName);
		exportParams.setCreateHeadRows(isCreateHeader);
		defaultExport(list, pojoClass, fileName, response, exportParams);

	}
	
	/**
	 * 导出多个Sheet页 
	 * @param workflowExcels 第一个 sheet 页
	 * @param workflowTemplateExcel 第二个 sheet 页
	 * @param response 响应
	 * @throws Exception 异常
	 */
	/*public static void exportExcel(List<WorkflowExcel> workflowExcels, WorkflowTemplateExcel workflowTemplateExcel, HttpServletResponse response) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		ExportParams exportParamsWorkflow = new ExportParams("流程配置, 补全PHASE_ID, PHASE_NAME, ROLE_CODE后自行导入数据库", "ALM_WORKFLOW");
		// 流程配置
		Map<String, Object> worflowMap = new HashMap<String, Object>();
		worflowMap.put("title", exportParamsWorkflow);
		worflowMap.put("entity", WorkflowExcel.class);
		worflowMap.put("data", workflowExcels);
		result.add(worflowMap);
		// 流程模版
		ExportParams exportParamsTemplate = new ExportParams("流程模版, 补全TEMPLATE_ID, OBJ_TYPE后自行导入数据库", "ALM_WORKFLOW_TEMPLATE");
		Map<String, Object> templateMap = new HashMap<String, Object>();
		templateMap.put("title", exportParamsTemplate);
		templateMap.put("entity", WorkflowTemplateExcel.class);
		List<WorkflowTemplateExcel> workflowTemplateExcels = new ArrayList<WorkflowTemplateExcel>();
		workflowTemplateExcels.add(workflowTemplateExcel);
		templateMap.put("data", workflowTemplateExcels);
		result.add(templateMap);
		Workbook workbook = ExcelExportUtil.exportExcel(result, ExcelType.HSSF);
		downLoadExcel("WorkFlowConfig", response, workbook);
	}*/

	public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName,
			HttpServletResponse response) throws Exception {
		defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName));
	}
	
	public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,
			FileOutputStream  outputStream) throws Exception {
		defaultExport(list, pojoClass,outputStream, new ExportParams(title, sheetName));
	}

	public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws Exception {
		defaultExport(list, fileName, response);
	}

	private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response,
			ExportParams exportParams) throws Exception {
		Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
		downLoadExcel(fileName, response, workbook);
	}
	
	private static void defaultExport(List<?> list, Class<?> pojoClass, FileOutputStream  outputStream,
			ExportParams exportParams) throws Exception {
		Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
		downLoadExcel(outputStream, workbook);
	}

	private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws Exception {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("content-Type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName+".xls", "UTF-8"));
			workbook.write(response.getOutputStream());
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		}
	}
	
	private static void downLoadExcel(FileOutputStream  outputStream, Workbook workbook) throws Exception {
		try {
			workbook.write(outputStream);
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		}
	}

	private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws Exception {
		Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
		downLoadExcel(fileName, response, workbook);
	}

	public static <T> List<T> importExcel(File file, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws Exception {
		ImportParams params = new ImportParams();
		params.setTitleRows(titleRows);
		params.setHeadRows(headerRows);
		List<T> list = null;
		try {
			list = ExcelImportUtil.importExcel(file, pojoClass, params);
		} catch (NoSuchElementException e) {
			throw new Exception("模板不能为空");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return list;
	}

	public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows,
			Class<T> pojoClass) throws Exception {
		if (file == null) {
			return null;
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(titleRows);
		params.setHeadRows(headerRows);
		List<T> list = null;
		try {
			list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
		} catch (NoSuchElementException e) {
			throw new Exception("excel文件不能为空");
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return list;
	}

	public static <T> List<T> formulaImportExcel(MultipartFile file, Integer titleRows, Integer headerRows,
			Class<T> pojoClass) throws Exception {
		if (file == null) {
			return null;
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(titleRows);
		params.setHeadRows(headerRows);
		List<T> list = null;
		try {
			list = FormulaExcelImportUtil.formulaImportExcel(file.getInputStream(), pojoClass, params);
		} catch (NoSuchElementException e) {
			throw new Exception("excel文件不能为空");
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return list;
	}
}
