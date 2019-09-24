package com.zos.activiti.excel.util;

import java.io.InputStream;
import java.util.List;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FormulaExcelImportUtil<T> {

    /**
     * Excel 导入 数据源IO流,不返回校验结果 导入 字段类型 Integer,Long,Double,Date,String,Boolean
     * 
     * @param inputstream
     * @param pojoClass
     * @param params
     * @return
     * @throws Exception
     */
    public static <T> List<T> formulaImportExcel(InputStream inputstream, Class<T> pojoClass,
                                          ImportParams params) throws Exception {
    	log.info("");
    	FormulaExcelImportService<T> FormulaExcelImportService = new FormulaExcelImportService<T>().getFormulaExcelImportService();
        return FormulaExcelImportService.formulaImportExcelByIs(inputstream, pojoClass, params, false).getList();
    }

}
