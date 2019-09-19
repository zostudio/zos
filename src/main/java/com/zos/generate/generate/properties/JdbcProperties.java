package com.zos.generate.generate.properties;

import lombok.Data;

/**
 * 支持多种数据库<br/>
 * <pre>
 * H2  
 *     url jdbc:h2:tcp://localhost/test
 *     driver org.h2.Driver
 * Mysql 
 *     url jdbc:mysql://localhost:3306/test?useUnicode=true&amp;characterEncoding=UTF-8
 *     driver com.mysql.jdbc.Driver
 * Oracle
 *     url jdbc:oracle:thin:@//10.1.195.102:1521/AIDB
 *     driver oracle.jdbc.driver.OracleDriver
 * SQLServer2000 
 *     url jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=[database]
 *     driver com.microsoft.jdbc.sqlserver.SQLServerDriver
 * SQLServer2005 
 *     url jdbc:sqlserver://192.168.0.98:1433;DatabaseName=[database]
 *     driver com.microsoft.sqlserver.jdbc.SQLServerDriver
 * JTDs for SQLServer 
 *     url jdbc:jtds:sqlserver://192.168.0.102:1433/[database];tds=8.0;lastupdatecount=true
 *     driver net.sourceforge.jtds.jdbc.Driver
 * PostgreSql
 *     url jdbc:postgresql://localhost/[database]
 *     driver org.postgresql.Driver
 * Sybase
 *     url jdbc:sybase:Tds:localhost:5007/[database]
 *     driver com.sybase.jdbc.SybDriver
 * DB2 
 *     url jdbc:db2://localhost:5000/[database]
 *     driver com.ibm.db2.jdbc.app.DB2Driver
 * HsqlDB 
 *     url jdbc:hsqldb:mem:generatorDB
 *     driver org.hsqldb.jdbcDriver
 * Derby 
 *     url jdbc:derby://localhost/databaseName
 *     driver org.apache.derby.jdbc.ClientDriver	
 * </pre>
 *
 */
@Data
public class JdbcProperties {
	
	private String username;
	
	private String password;
	
	/**
	 * Oracle 必填, 其他数据库可忽略
	 */
	private String schema;
	
	/**
	 * Oracle 必填, 其他数据库可忽略
	 */
	private String catalog;
	
	private String url;
	
	private String driver;
	
}
