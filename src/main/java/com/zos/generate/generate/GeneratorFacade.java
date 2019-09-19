package com.zos.generate.generate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.zos.generate.common.ProcessGenerateModel;
import com.zos.generate.generate.Generator.GeneratorModel;
import com.zos.generate.generate.properties.GenerateProperties;
import com.zos.generate.generate.provider.db.sql.model.Sql;
import com.zos.generate.generate.provider.db.table.TableFactory;
import com.zos.generate.generate.provider.db.table.model.Table;
import com.zos.generate.generate.provider.java.model.JavaClass;
import com.zos.generate.generate.util.BeanHelper;
import com.zos.generate.generate.util.GeneratorException;
import com.zos.generate.generate.util.SpringHelper;
import com.zos.generate.generate.util.typemapping.DatabaseTypeUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GeneratorFacade {

	public Generator g = new Generator();

	public GeneratorFacade(GenerateProperties generateProperties) {
		g.setOutRootDir(generateProperties.getOutUri());
		g.setProcessDir(generateProperties.getProcess());
	}

	public static void printAllTableNames() throws Exception {
		PrintUtils.printAllTableNames(TableFactory.getInstance().getAllTables());
	}

	public void deleteOutRootDir() throws IOException {
		g.deleteOutRootDir();
	}

	@SuppressWarnings("rawtypes")
	public void generateByMap(Map map, String templateRootDir) throws Exception {
		new ProcessUtils().processByMap(map, templateRootDir, false);
	}

	@SuppressWarnings("rawtypes")
	public void deleteByMap(Map map, String templateRootDir) throws Exception {
		new ProcessUtils().processByMap(map, templateRootDir, true);
	}

	public void generateByAllTable(String templateRootDir) throws Exception {
		new ProcessUtils().processByAllTable(templateRootDir, false);
	}

	public void deleteByAllTable(String templateRootDir) throws Exception {
		new ProcessUtils().processByAllTable(templateRootDir, true);
	}

	public void generateByTable(String tableName, String templateRootDir) throws Exception {
		new ProcessUtils().processByTable(tableName, templateRootDir, false);
	}

	public void deleteByTable(String tableName, String templateRootDir) throws Exception {
		new ProcessUtils().processByTable(tableName, templateRootDir, true);
	}

	public void generateByProcessDiagram(ProcessGenerateModel processGenerateModel, String templateRootDir, Map<String, Object> processModel) throws Exception {
		new ProcessUtils().processByProcessDiagram(processGenerateModel, templateRootDir, false, processModel);
	}

	public void deleteByProcessDiagram(ProcessGenerateModel processGenerateModel, String templateRootDir, Map<String, Object> processModel) throws Exception {
		new ProcessUtils().processByProcessDiagram(processGenerateModel, templateRootDir, true, processModel);
	}

	public void generateByClass(Class<?> clazz, String templateRootDir) throws Exception {
		new ProcessUtils().processByClass(clazz, templateRootDir, false);
	}

	public void deleteByClass(Class<?> clazz, String templateRootDir) throws Exception {
		new ProcessUtils().processByClass(clazz, templateRootDir, true);
	}

	public void generateBySql(Sql sql, String templateRootDir) throws Exception {
		new ProcessUtils().processBySql(sql, templateRootDir, false);
	}

	public void deleteBySql(Sql sql, String templateRootDir) throws Exception {
		new ProcessUtils().processBySql(sql, templateRootDir, true);
	}

	private Generator getGenerator(String templateRootDir) {
		g.setTemplateRootDir(new File(templateRootDir).getAbsoluteFile());
		return g;
	}

	/** 生成器的上下文，存放的变量将可以在模板中引用 */
	public static class GeneratorContext {
		@SuppressWarnings("rawtypes")
		static ThreadLocal<Map> context = new ThreadLocal<Map>();

		@SuppressWarnings("rawtypes")
		public static void clear() {
			Map m = context.get();
			if (m != null)
				m.clear();
		}

		@SuppressWarnings("rawtypes")
		public static Map getContext() {
			Map map = context.get();
			if (map == null) {
				setContext(new HashMap());
			}
			return context.get();
		}

		@SuppressWarnings("rawtypes")
		public static void setContext(Map map) {
			context.set(map);
		}

		@SuppressWarnings("unchecked")
		public static void put(String key, Object value) {
			getContext().put(key, value);
		}
	}

	public class ProcessUtils {
		@SuppressWarnings("rawtypes")
		public void processByMap(Map params, String templateRootDir, boolean isDelete)
				throws Exception, FileNotFoundException {
			Generator g = getGenerator(templateRootDir);
			GeneratorModel m = GeneratorModelUtils.newFromMap(params);
			try {
				if (isDelete)
					g.deleteBy(m.templateModel, m.filePathModel);
				else
					g.generateBy(m.templateModel, m.filePathModel);
			} catch (GeneratorException ge) {
				PrintUtils.printExceptionsSumary(ge.getMessage(), getGenerator(templateRootDir).getOutRootDir(),
						ge.getExceptions());
			}
		}

		public void processBySql(Sql sql, String templateRootDir, boolean isDelete) throws Exception {
			Generator g = getGenerator(templateRootDir);
			GeneratorModel m = GeneratorModelUtils.newFromSql(sql);
			PrintUtils.printBeginProcess("sql:" + sql.getSourceSql(), isDelete);
			try {
				if (isDelete) {
					g.deleteBy(m.templateModel, m.filePathModel);
				} else {
					g.generateBy(m.templateModel, m.filePathModel);
				}
			} catch (GeneratorException ge) {
				PrintUtils.printExceptionsSumary(ge.getMessage(), getGenerator(templateRootDir).getOutRootDir(),
						ge.getExceptions());
			}
		}

		public void processByClass(Class<?> clazz, String templateRootDir, boolean isDelete)
				throws Exception, FileNotFoundException {
			Generator g = getGenerator(templateRootDir);
			GeneratorModel m = GeneratorModelUtils.newFromClass(clazz);
			PrintUtils.printBeginProcess("JavaClass:" + clazz.getSimpleName(), isDelete);
			try {
				if (isDelete)
					g.deleteBy(m.templateModel, m.filePathModel);
				else
					g.generateBy(m.templateModel, m.filePathModel);
			} catch (GeneratorException ge) {
				PrintUtils.printExceptionsSumary(ge.getMessage(), getGenerator(templateRootDir).getOutRootDir(),
						ge.getExceptions());
			}
		}

		public void processByTable(String tableName, String templateRootDir, boolean isDelete) throws Exception {
			if ("*".equals(tableName)) {
				generateByAllTable(templateRootDir);
				return;
			}
			Generator g = getGenerator(templateRootDir);
			Table table = TableFactory.getInstance().getTable(tableName);
			try {
				processByTable(g, table, isDelete);
			} catch (GeneratorException ge) {
				PrintUtils.printExceptionsSumary(ge.getMessage(), getGenerator(templateRootDir).getOutRootDir(),
						ge.getExceptions());
			}
		}

		public void processByProcessDiagram(ProcessGenerateModel processGenerateModel, String templateRootDir, boolean isDelete, Map<String, Object> processModel) throws Exception {
			
			Generator g = getGenerator(templateRootDir);
			try {
				processByProcessDiagram(g, processGenerateModel, isDelete, processModel);
			} catch (GeneratorException ge) {
				PrintUtils.printExceptionsSumary(ge.getMessage(), getGenerator(templateRootDir).getOutRootDir(),
						ge.getExceptions());
			}
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void processByAllTable(String templateRootDir, boolean isDelete) throws Exception {
			List<Table> tables = TableFactory.getInstance().getAllTables();
			List exceptions = new ArrayList();
			for (int i = 0; i < tables.size(); i++) {
				try {
					processByTable(getGenerator(templateRootDir), tables.get(i), isDelete);
				} catch (GeneratorException ge) {
					exceptions.addAll(ge.getExceptions());
				}
			}
			PrintUtils.printExceptionsSumary("", getGenerator(templateRootDir).getOutRootDir(), exceptions);
		}

		public void processByTable(Generator g, Table table, boolean isDelete) throws Exception {
			GeneratorModel m = GeneratorModelUtils.newFromTable(table);
			PrintUtils.printBeginProcess(table.getSqlName() + " => " + table.getClassName(), isDelete);
			if (isDelete)
				g.deleteBy(m.templateModel, m.filePathModel);
			else
				g.generateBy(m.templateModel, m.filePathModel);
		}
		
		public void processByProcessDiagram(Generator g, ProcessGenerateModel processGenerateModel, boolean isDelete, Map<String, Object> processModel) throws Exception {
			GeneratorModel m = GeneratorModelUtils.newProcessFromDiagram(processGenerateModel, processModel);
			PrintUtils.printBeginProcess("流程主键: "+processGenerateModel.getWorkflowTemplate().getProcessKey()+ " => "+processGenerateModel.getWorkflowTemplate().getProcessName(), isDelete);
			if (isDelete)
				g.deleteBy(m.templateModel, m.filePathModel);
			else
				g.generateBy(m.templateModel, m.filePathModel);
		}
	}

	public static class GeneratorModelUtils {
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public static GeneratorModel newProcessFromDiagram(ProcessGenerateModel processGenerateModel, Map<String, Object> processModel) {
			Map templateModel = new HashMap();
			templateModel.putAll(processModel);
			templateModel.put("processGM", processGenerateModel);
			setShareVars(templateModel);
			Map filePathModel = new HashMap();
			filePathModel.putAll(processModel);
			setShareVars(filePathModel);
			filePathModel.putAll(BeanHelper.describe(processGenerateModel));
			return new GeneratorModel(templateModel, filePathModel);
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public static GeneratorModel newFromTable(Table table) {
			Map templateModel = new HashMap();
			templateModel.put("table", table);
			setShareVars(templateModel);

			Map filePathModel = new HashMap();
			setShareVars(filePathModel);
			filePathModel.putAll(BeanHelper.describe(table));
			return new GeneratorModel(templateModel, filePathModel);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public static GeneratorModel newFromSql(Sql sql) throws Exception {
			Map templateModel = new HashMap();
			templateModel.put("sql", sql);
			setShareVars(templateModel);

			Map filePathModel = new HashMap();
			setShareVars(filePathModel);
			filePathModel.putAll(BeanHelper.describe(sql));
			return new GeneratorModel(templateModel, filePathModel);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public static GeneratorModel newFromClass(Class clazz) {
			Map templateModel = new HashMap();
			templateModel.put("clazz", new JavaClass(clazz));
			setShareVars(templateModel);

			Map filePathModel = new HashMap();
			setShareVars(filePathModel);
			filePathModel.putAll(BeanHelper.describe(new JavaClass(clazz)));
			return new GeneratorModel(templateModel, filePathModel);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public static GeneratorModel newFromMap(Map params) {
			Map templateModel = new HashMap();
			templateModel.putAll(params);
			setShareVars(templateModel);

			Map filePathModel = new HashMap();
			setShareVars(filePathModel);
			filePathModel.putAll(params);
			return new GeneratorModel(templateModel, filePathModel);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public static void setShareVars(Map templateModel) {
			//templateModel.putAll(generateProperties.getProperties());
			GenerateProperties generateProperties = (GenerateProperties) SpringHelper.getBean("generateProperties");
			StringBuffer basepackageDir = new StringBuffer();
			String separator = String.valueOf(File.separatorChar);
			if(generateProperties.getBasePackage().indexOf(".") < 0) {
				basepackageDir.append(generateProperties.getBasePackage());
                basepackageDir.append(separator);
			} else {
				String[] elements = generateProperties.getBasePackage().split("\\.");
				int size = elements.length;
				for (String element : elements) {
					basepackageDir.append(element);
					if (--size > 0) {
						basepackageDir.append(separator);
					}
				}
	        }
			templateModel.put("basepackage_dir", basepackageDir.toString());
			templateModel.put("basepackage", generateProperties.getBasePackage());
			templateModel.put("process", generateProperties.getProcess());
			templateModel.putAll(System.getProperties());
			templateModel.put("env", System.getenv());
			templateModel.put("now", new Date());
			templateModel.put("databaseType", getDatabaseType("databaseType"));
			templateModel.putAll(GeneratorContext.getContext());
		}

		private static String getDatabaseType(String key) {
			GenerateProperties generateProperties = (GenerateProperties) SpringHelper.getBean("generateProperties");
			return DatabaseTypeUtils.getDatabaseTypeByJdbcDriver(generateProperties.getJdbc().getDriver());
		}

	}

	private static class PrintUtils {

		private static void printExceptionsSumary(String msg, String outRoot, List<Exception> exceptions)
				throws FileNotFoundException {
			File errorFile = new File(outRoot, "generator_error.log");
			if (exceptions != null && exceptions.size() > 0) {
				log.info("[Generate Error Summary] : " + msg);
				PrintStream output = new PrintStream(new FileOutputStream(errorFile));
				for (int i = 0; i < exceptions.size(); i++) {
					Exception e = exceptions.get(i);
					log.info("[GENERATE ERROR]:" + e);
					if (i == 0)
						e.printStackTrace();
					e.printStackTrace(output);
				}
				output.close();
				log.info("***************************************************************");
				log.info("* " + "* 输出目录已经生成generator_error.log用于查看错误 ");
				log.info("***************************************************************");
			}
		}

		private static void printBeginProcess(String displayText, boolean isDatele) {
			log.info("***************************************************************");
			log.info("* BEGIN " + (isDatele ? " delete by " : " generate by ") + displayText);
			log.info("***************************************************************");
		}

		public static void printAllTableNames(List<Table> tables) throws Exception {
			log.info("----All TableNames BEGIN----");
			for (int i = 0; i < tables.size(); i++) {
				String sqlName = tables.get(i).getSqlName();
				log.info("g.generateTable(\"" + sqlName + "\");");
			}
			log.info("----All TableNames END----");
		}
	}

}
