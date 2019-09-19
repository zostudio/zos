package com.zos.generate.generate.provider.db.table.model;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import com.zos.generate.generate.properties.GenerateProperties;
import com.zos.generate.generate.provider.db.table.TableFactory;
import com.zos.generate.generate.util.SpringHelper;
import com.zos.generate.generate.util.StringHelper;

/**
 * 用于生成代码的Table对象, 对应数据库的table
 */
public class Table {

	String sqlName;
	String remarks;
	String className;
	/** the name of the owner of the synonym if this table is a synonym */
	private String ownerSynonymName = null;
	LinkedHashSet<Column> columns = new LinkedHashSet<Column>();
	List<String> primaryKeyColumnsName = new ArrayList<String>();

	public Table() {}

	public Table(Table t) {
		setSqlName(t.getSqlName());
		this.remarks = t.getRemarks();
		this.className = t.getSqlName();
		this.ownerSynonymName = t.getOwnerSynonymName();
		this.columns = t.getColumns();
		this.primaryKeyColumnsName = t.getPrimaryKeyColumnsName();
		this.tableAlias = t.getTableAlias();
		this.exportedKeys = t.exportedKeys;
		this.importedKeys = t.importedKeys;
	}

	public LinkedHashSet<Column> getColumns() {
		return columns;
	}

	public void setColumns(LinkedHashSet<Column> columns) {
		this.columns = columns;
	}

	public String getOwnerSynonymName() {
		return ownerSynonymName;
	}

	public void setOwnerSynonymName(String ownerSynonymName) {
		this.ownerSynonymName = ownerSynonymName;
	}

	/** 数据库中表的表名称,其它属性很多都是根据此属性派生 */
	public String getSqlName() {
		return sqlName;
	}

	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}

	public static String removeTableSqlNamePrefix(String sqlName) {
		GenerateProperties generateProperties = (GenerateProperties) SpringHelper.getBean("generateProperties");
		String prefixs = generateProperties.getTableRemovePrefixes();
		for (String prefix : prefixs.split(",")) {
			String removedPrefixSqlName = StringHelper.removePrefix(sqlName, prefix, true);
			if (!removedPrefixSqlName.equals(sqlName)) {
				return removedPrefixSqlName;
			}
		}
		return sqlName;
	}

	/** 数据库中表的表备注 */
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void addColumn(Column column) {
		columns.add(column);
	}

	public void setClassName(String customClassName) {
		this.className = customClassName;
	}

	/**
	 * 根据sqlName得到的类名称，示例值: UserInfo
	 * 
	 * @return
	 */
	public String getClassName() {
		if (StringHelper.isBlank(className)) {
			String removedPrefixSqlName = removeTableSqlNamePrefix(sqlName);
			className = StringHelper
					.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(removedPrefixSqlName));
		}
		return className;
	}

	/** 数据库中表的别名，等价于: getRemarks().isEmpty() ? getClassName() : getRemarks() */
	public String getTableAlias() {
		if (StringHelper.isNotBlank(tableAlias))
			return tableAlias;
		return StringHelper.removeCrlf(StringHelper.defaultIfEmpty(getRemarks(), getClassName()));
	}

	public void setTableAlias(String v) {
		this.tableAlias = v;
	}

	/**
	 * 等价于getClassName().toLowerCase()
	 * 
	 * @return
	 */
	public String getClassNameLowerCase() {
		return getClassName().toLowerCase();
	}

	/**
	 * 得到用下划线分隔的类名称，如className=UserInfo,则underscoreName=user_info
	 * 
	 * @return
	 */
	public String getUnderscoreName() {
		return StringHelper.toUnderscoreName(getClassName()).toLowerCase();
	}

	/**
	 * 返回值为getClassName()的第一个字母小写,如className=UserInfo,则ClassNameFirstLower=userInfo
	 * 
	 * @return
	 */
	public String getClassNameFirstLower() {
		return StringHelper.uncapitalize(getClassName());
	}

	/**
	 * 根据getClassName()计算而来,用于得到常量名,如className=UserInfo,则constantName=USER_INFO
	 * 
	 * @return
	 */
	public String getConstantName() {
		return StringHelper.toUnderscoreName(getClassName()).toUpperCase();
	}

	/** 使用 getPkCount() 替换 */
	@Deprecated
	public boolean isSingleId() {
		return getPkCount() == 1 ? true : false;
	}

	/** 使用 getPkCount() 替换 */
	@Deprecated
	public boolean isCompositeId() {
		return getPkCount() > 1 ? true : false;
	}

	/** 使用 getPkCount() 替换 */
	@Deprecated
	public boolean isNotCompositeId() {
		return !isCompositeId();
	}

	/**
	 * 得到主键总数
	 * 
	 * @return
	 */
	public int getPkCount() {
		int pkCount = 0;
		for (Column c : columns) {
			if (c.isPk()) {
				pkCount++;
			}
		}
		return pkCount;
	}

	/**
	 * 得到是主键的全部column
	 * 
	 * @return
	 */
	public List<Column> getPkColumns() {
		List<Column> results = new ArrayList<Column>();
		for (Column c : getColumns()) {
			if (c.isPk())
				results.add(c);
		}
		return results;
	}
	
	/**
	 * 设置 主键名称集合
	 * @param primaryKeyColumns 主键名称
	 */
	public void setPrimaryKeyColumnsName(List<String> primaryKeyColumnsName) {
		this.primaryKeyColumnsName = primaryKeyColumnsName;
	}
	
	public List<String> getPrimaryKeyColumnsName() {
		return this.primaryKeyColumnsName;
	}

	/**
	 * 得到不是主键的全部column
	 * 
	 * @return
	 */
	public List<Column> getNotPkColumns() {
		List<Column> results = new ArrayList<Column>();
		for (Column c : getColumns()) {
			if (!c.isPk())
				results.add(c);
		}
		return results;
	}

	/** 得到单主键, 等价于getPkColumns().get(0) */
	public Column getPkColumn() {
		if (getPkColumns().isEmpty()) {
			return null;
		}
		return getPkColumns().get(0);
	}

	public boolean getHasDateTimeColumn() {
		for (Column column : getColumns()) {
			if (column.getIsDateTimeColumn()) {
				return true;
			}
		}
		return false;
	}

	public Integer getDateTimeColumnCount() {
		int count = 0;
		for (Column column : getColumns()) {
			if (column.getIsDateTimeColumn()) {
				count++;
			}
		}
		return count;
	}

	public boolean getHasStringColumn() {
		for (Column column : getColumns()) {
			if (column.getIsStringColumn()) {
				return true;
			}
		}
		return false;
	}

	public List<Column> getEnumColumns() {
		List<Column> results = new ArrayList<Column>();
		for (Column c : getColumns()) {
			if (!c.isEnumColumn())
				results.add(c);
		}
		return results;
	}

	public Column getColumnByName(String name) {
		Column c = getColumnBySqlName(name);
		if (c == null) {
			c = getColumnBySqlName(StringHelper.toUnderscoreName(name));
		}
		return c;
	}

	public Column getColumnBySqlName(String sqlName) {
		for (Column c : getColumns()) {
			if (c.getSqlName().equalsIgnoreCase(sqlName)) {
				return c;
			}
		}
		return null;
	}

	public Column getRequiredColumnBySqlName(String sqlName) {
		if (getColumnBySqlName(sqlName) == null) {
			throw new IllegalArgumentException(
					"not found column with sqlName:" + sqlName + " on table:" + getSqlName());
		}
		return getColumnBySqlName(sqlName);
	}

	/**
	 * 忽略过滤掉某些关键字的列,关键字不区分大小写,以逗号分隔
	 * 
	 * @param ignoreKeywords
	 * @return
	 */
	public List<Column> getIgnoreKeywordsColumns(String ignoreKeywords) {
		List<Column> results = new ArrayList<Column>();
		for (Column c : getColumns()) {
			String sqlname = c.getSqlName().toLowerCase();
			if (StringHelper.contains(sqlname, ignoreKeywords.split(","))) {
				continue;
			}
			results.add(c);
		}
		return results;
	}

	/**
	 * This method was created in VisualAge.
	 */
	public void initImportedKeys(DatabaseMetaData dbmd) throws java.sql.SQLException {

		// get imported keys a

		ResultSet fkeys = dbmd.getImportedKeys(catalog, schema, this.sqlName);

		while (fkeys.next()) {
			String pktable = fkeys.getString(PKTABLE_NAME);
			String pkcol = fkeys.getString(PKCOLUMN_NAME);
			//String fktable = fkeys.getString(FKTABLE_NAME);
			String fkcol = fkeys.getString(FKCOLUMN_NAME);
			String seq = fkeys.getString(KEY_SEQ);
			Integer iseq = new Integer(seq);
			getImportedKeys().addForeignKey(pktable, pkcol, fkcol, iseq);
		}
		fkeys.close();
	}

	/**
	 * This method was created in VisualAge.
	 */
	public void initExportedKeys(DatabaseMetaData dbmd) throws java.sql.SQLException {
		// get Exported keys

		ResultSet fkeys = dbmd.getExportedKeys(catalog, schema, this.sqlName);

		while (fkeys.next()) {
			//String pktable = fkeys.getString(PKTABLE_NAME);
			String pkcol = fkeys.getString(PKCOLUMN_NAME);
			String fktable = fkeys.getString(FKTABLE_NAME);
			String fkcol = fkeys.getString(FKCOLUMN_NAME);
			String seq = fkeys.getString(KEY_SEQ);
			Integer iseq = new Integer(seq);
			getExportedKeys().addForeignKey(fktable, fkcol, pkcol, iseq);
		}
		fkeys.close();
	}

	/**
	 * @return Returns the exportedKeys.
	 */
	public ForeignKeys getExportedKeys() {
		if (exportedKeys == null) {
			exportedKeys = new ForeignKeys(this);
		}
		return exportedKeys;
	}

	/**
	 * @return Returns the importedKeys.
	 */
	public ForeignKeys getImportedKeys() {
		if (importedKeys == null) {
			importedKeys = new ForeignKeys(this);
		}
		return importedKeys;
	}

	@Override
	public String toString() {
		return "Database Table:" + getSqlName() + " to ClassName:" + getClassName();
	}

	String catalog = TableFactory.getInstance().getCatalog();
	String schema = TableFactory.getInstance().getSchema();

	private String tableAlias;
	private ForeignKeys exportedKeys;
	private ForeignKeys importedKeys;

	public static final String PKTABLE_NAME = "PKTABLE_NAME";
	public static final String PKCOLUMN_NAME = "PKCOLUMN_NAME";
	public static final String FKTABLE_NAME = "FKTABLE_NAME";
	public static final String FKCOLUMN_NAME = "FKCOLUMN_NAME";
	public static final String KEY_SEQ = "KEY_SEQ";
}
