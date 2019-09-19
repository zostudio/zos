package com.zos.generate.generate;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.xml.sax.InputSource;

import com.zos.generate.generate.properties.GenerateProperties;
import com.zos.generate.generate.provider.db.table.TableFactory;
import com.zos.generate.generate.util.FileHelper;
import com.zos.generate.generate.util.IOHelper;
import com.zos.generate.generate.util.SpringHelper;
import com.zos.generate.generate.util.SqlExecutorHelper;
import com.zos.generate.generate.util.XMLHelper;

import freemarker.ext.dom.NodeModel;
import lombok.extern.slf4j.Slf4j;

/**
 * 生成器模板控制器,用于模板中可以控制生成器执行相关控制操作
 * 如: 是否覆盖目标文件
 * 
 * <pre>
 * 使用方式:
 * 可以在freemarker或是veloctiy中直接控制模板的生成
 * 
 * ${gg.generateFile('d:/g_temp.log','info_from_generator')}
 * ${gg.setIgnoreOutput(true)}
 * </pre>
 * 
 * ${gg.setIgnoreOutput(true)}将设置为true如果不生成
 * 
 * @author 
 *
 */
@Slf4j
public class GeneratorControl {
	private boolean isOverride = Boolean.parseBoolean(System.getProperty("gg.isOverride","false")); 
	@SuppressWarnings("unused")
	private boolean isAppend = false; //no pass
	private boolean ignoreOutput = false; 
	private boolean isMergeIfExists = true; //no pass
	private String mergeLocation; 
	private String outRoot; 
	private String process; 
	private String outputEncoding; 
	private String sourceFile; 
	private String sourceDir; 
	private String sourceFileName; 
	private String sourceEncoding; //no pass //? 难道process两次确定sourceEncoding
	@SuppressWarnings("unused")
	private GenerateProperties generateProperties = (GenerateProperties) SpringHelper.getBean("generateProperties");
	
	/** load xml data */
	public NodeModel loadXml(String file) {
		return loadXml(file,true);
	}	
	/** load xml data */
	public NodeModel loadXml(String file,boolean removeXmlNamespace) {
		try {
			if(removeXmlNamespace) {
				InputStream forEncodingInput = FileHelper.getInputStream(file);
				String encoding = XMLHelper.getXMLEncoding(forEncodingInput);
				forEncodingInput.close();
				
				InputStream input = FileHelper.getInputStream(file);
				String xml = IOHelper.toString(encoding,input);
				xml = XMLHelper.removeXmlns(xml);
				input.close();
				return NodeModel.parse(new InputSource(new StringReader(xml.trim())));
			}else {
				return NodeModel.parse(new InputSource(FileHelper.getInputStream(file)));
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("loadXml error,file:"+file,e);
		}
	}

	/** load Properties data */
	public Properties loadProperties(String file) {
		try {
			Properties p = new Properties();
			InputStream in = FileHelper.getInputStream(file);
			if(file.endsWith(".xml")) {
				p.loadFromXML(in);
			}else {
				p.load(in);
			}
			in.close();
			return p;
		} catch (Exception e) {
			throw new IllegalArgumentException("loadProperties error,file:"+file,e);
		}
	}

    public void generateFile(String outputFile,String content) {
       generateFile(outputFile,content,false);
    }
	/**
	 * 生成文件   
	 * @param outputFile
	 * @param content
	 * @param append
	 */
	public void generateFile(String outputFile,String content,boolean append) {
		try {
			String realOutputFile = null;
			if(new File(outputFile).isAbsolute()) {
				realOutputFile = outputFile ;
			}else {
				realOutputFile = new File(getOutRoot(),outputFile).getAbsolutePath();
			}
			
			if(deleteGeneratedFile) {
				log.info("[delete gg.generateFile()] file:"+realOutputFile+" by template:"+getSourceFile());
				new File(realOutputFile).delete();
			}else {
				File file = new File(realOutputFile);
				FileHelper.parnetMkdir(file);
				log.info("[gg.generateFile()] outputFile:"+realOutputFile+" append:"+append+" by template:"+getSourceFile());
				IOHelper.saveFile(file, content,getOutputEncoding(),append);
			}
		} catch (Exception e) {
			log.warn("gg.generateFile() occer error,outputFile:"+outputFile+" caused by:"+e,e);
			throw new RuntimeException("gg.generateFile() occer error,outputFile:"+outputFile+" caused by:"+e,e);
		}
	}
	
	public boolean isOverride() {
		return isOverride;
	}
	/**如果目标文件存在,控制是否要覆盖文件 */
	public void setOverride(boolean isOverride) {
		this.isOverride = isOverride;
	}

	public boolean isIgnoreOutput() {
		return ignoreOutput;
	}
	/** 控制是否要生成文件  */
	public void setIgnoreOutput(boolean ignoreOutput) {
		this.ignoreOutput = ignoreOutput;
	}

	public boolean isMergeIfExists() {
		return isMergeIfExists;
	}

	public void setMergeIfExists(boolean isMergeIfExists) {
		this.isMergeIfExists = isMergeIfExists;
	}

	public String getMergeLocation() {
		return mergeLocation;
	}

	public void setMergeLocation(String mergeLocation) {
		this.mergeLocation = mergeLocation;
	}

	public String getOutRoot() {
		return outRoot;
	}
	/** 生成的输出根目录 */
	public void setOutRoot(String outRoot) {
		this.outRoot = outRoot;
	}

	public String getProcess() {
		return process;
	}
	/** 生成的输出根目录 */
	public void setProcess(String process) {
		this.process = process;
	}

	public String getOutputEncoding() {
		return outputEncoding;
	}
	/** 设置输出encoding */
	public void setOutputEncoding(String outputEncoding) {
		this.outputEncoding = outputEncoding;
	}
	/** 得到源文件 */
	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	/** 得到源文件所在目录 */
	public String getSourceDir() {
		return sourceDir;
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	/** 得到源文件的文件名称 */
	public String getSourceFileName() {
		return sourceFileName;
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	/** 得到源文件的encoding */
	public String getSourceEncoding() {
		return sourceEncoding;
	}

	public void setSourceEncoding(String sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map> queryForList(String sql,int limit) throws SQLException {
		Connection conn = TableFactory.getInstance().getConnection();
		return SqlExecutorHelper.queryForList(conn, sql, limit);
	}
	
	boolean deleteGeneratedFile = false;
}
