<#assign className = table.className>
<#assign tableRemarks = table.remarks?default("暂无表注释")>
<#assign hasDateTimeColumn = table.hasDateTimeColumn>
<#assign hasStringColumn = table.hasStringColumn>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.dao.${process};

import org.springframework.stereotype.Repository;

import ${basepackage}.dao.common.CommonDAO;
import ${basepackage}.domain.${process}.BO${className};

/**
 * ${tableRemarks}
 * 
 * @author 01Studio
 * @version 1.0
 * @since 1.0
 */
@Repository
public class ${className}DAO extends CommonDAO<BO${className}> {

}
