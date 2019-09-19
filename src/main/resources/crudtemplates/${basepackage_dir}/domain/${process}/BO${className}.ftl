<#assign className = table.className>
<#assign tableRemarks = table.remarks?default("")>
<#assign hasDateTimeColumn = table.hasDateTimeColumn>
<#assign hasStringColumn = table.hasStringColumn>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.domain.${process};

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
<#if hasDateTimeColumn>
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
</#if>

<#if hasStringColumn>
import org.hibernate.validator.constraints.Length;
</#if>

/**
 * ${tableRemarks}
 * 
 * @author 01Studio
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "${table.sqlName}")
public class BO${className} {
	
	<@generateFields/>
	<@generatePkProperties/>
	<@generateNotPkProperties/>
}

<#macro generateFields>
	<#list table.columns as column>
	// ${column.remarks?default("")}
	${column.hibernateValidatorExprssion}
	private ${column.javaType} ${column.columnNameLower};
	</#list>
</#macro>

<#macro generatePkProperties>
	<#list table.columns as column>
		<#if column.pk>
	
	public void set${column.columnName}(${column.javaType} value) {
		this.${column.columnNameLower} = value;
	}
	
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE,generator="${table.sqlName}_SEQ")
	@SequenceGenerator(sequenceName="${table.sqlName}$SEQ",allocationSize=1,name="${table.sqlName}_SEQ")
	@Column(name = "${column.sqlName}", unique = ${column.unique?string}, nullable = ${column.nullable?string}, insertable = true, updatable = true, length = ${column.size})
	public ${column.javaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
		</#if>
	</#list>
</#macro>

<#macro generateNotPkProperties>
	<#list table.columns as column>
		<#if !column.pk>
		    <#if table.pkCount==0 && column_index==0>

	public void set${column.columnName}(${column.javaType} value) {
		this.${column.columnNameLower} = value;
	}
	
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE,generator="${table.sqlName}_SEQ")
	@SequenceGenerator(sequenceName="${table.sqlName}$SEQ",allocationSize=1,name="${table.sqlName}_SEQ")
	@Column(name = "${column.sqlName}", unique = ${column.unique?string}, nullable = ${column.nullable?string}, insertable = true, updatable = true, length = ${column.size})
	public ${column.javaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}	    
		    <#elseif column.isDateTimeColumn>
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "${column.sqlName}", unique = ${column.unique?string}, nullable = ${column.nullable?string}, insertable = true, updatable = true)
	public ${column.javaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
	
	public void set${column.columnName}(${column.javaType} value) {
		this.${column.columnNameLower} = value;
	}
			<#else>
	
	@Column(name = "${column.sqlName}", unique = ${column.unique?string}, nullable = ${column.nullable?string}, insertable = true, updatable = true, length = ${column.size})
	public ${column.javaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
	
	public void set${column.columnName}(${column.javaType} value) {
		this.${column.columnNameLower} = value;
	}
			</#if>
		</#if>
	</#list>
</#macro>