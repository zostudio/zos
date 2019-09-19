/**
 * 
 */
package com.zos.activiti;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.activiti.dmn.api.DmnDecisionTable;
import org.activiti.dmn.api.DmnDeployment;
import org.activiti.dmn.api.DmnRepositoryService;
import org.activiti.dmn.api.DmnRuleService;
import org.activiti.dmn.api.RuleEngineExecutionResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zos.activiti.beans.MvelPersion;
import com.zos.activiti.function.Sale;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 01Studio
 *
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiDmnTest {

	@Autowired
	private DmnRepositoryService dmnRepositoryService;

	@Autowired
	private DmnRuleService dmnRuleService;

	@Test
	public void contextLoads() {
		// 进行规则 部署
		DmnDeployment dmnDeployment = dmnRepositoryService.createDeployment().addClasspathResource("dmn/human.dmn")
				.deploy();
		// 进行数据查询
		DmnDecisionTable dmnDecisionTable = dmnRepositoryService.createDecisionTableQuery()
				.deploymentId(dmnDeployment.getId()).singleResult();
		// 初始化参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("personAge", 19);
		// 传入参数执行决策，并返回结果
		RuleEngineExecutionResult result = dmnRuleService.executeDecisionByKey(dmnDecisionTable.getKey(), params);
		// 控制台输出结果
		log.info("myResult {}", result.getResultVariables().get("myResult"));
		// 重新设置参数
		params.put("personAge", 5);
		// 重新执行决策
		result = dmnRuleService.executeDecisionByKey(dmnDecisionTable.getKey(), params);
		// 控制台重新输出结果
		log.info("myResult {}", result.getResultVariables().get("myResult"));

		// mvel 编译表达式
		Serializable compiledExpression = MVEL.compileExpression("mvelPersion.age >= 18");
		params.clear();
		MvelPersion mvelPersion = new MvelPersion();
		mvelPersion.setAge(30);
		params.put("mvelPersion", mvelPersion);
		// 执行表达式并返回结果
		Boolean mvelResult = MVEL.executeExpression(compiledExpression, params, Boolean.class);
		log.info("MVEL Result {}", mvelResult);

		Method method = getMethod(ActivitiDmnTest.class, "testMethod", String.class, Integer.class);
		// 创建解析上下文对象
		ParserContext parserContext = new ParserContext();
		// 添加方法导入
		parserContext.addImport("fn_abc", method);
		compiledExpression = MVEL.compileExpression("fn_abc('angus', 33)", parserContext);
		log.info("MVEL Result {}", MVEL.executeExpression(compiledExpression, null, String.class));

		// 进行规则 部署
		dmnDeployment = dmnRepositoryService.createDeployment().addClasspathResource("dmn/person.dmn").deploy();
		// 根据部署对象查询决策表
		dmnDecisionTable = dmnRepositoryService.createDecisionTableQuery().deploymentId(dmnDeployment.getId()).singleResult();
		// 设置参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("personName", "Angus");
		vars.put("age", 33);
		// 运行决策表
		RuleEngineExecutionResult ruleEngineExecutionResult = dmnRuleService.executeDecisionByKey(dmnDecisionTable.getKey(), vars);
		log.info("RuleEngineExecutionResult {}", ruleEngineExecutionResult.getResultVariables().get("outputResult"));
		
		dmnDeployment = dmnRepositoryService.createDeployment().addClasspathResource("dmn/sale.dmn").deploy();
        // 根据部署对象查询决策表
        dmnDecisionTable = dmnRepositoryService.createDecisionTableQuery().deploymentId(dmnDeployment.getId()).singleResult();
        Sale sale = new Sale();
        sale.setIdentity("gold");
        sale.setAmount(100);
        vars = new HashMap<String, Object>();
        vars.put("sale", sale);
        ruleEngineExecutionResult = dmnRuleService.executeDecisionByKey(dmnDecisionTable.getKey(), vars);
        Double resultMoney = (Double) ruleEngineExecutionResult.getResultVariables().get("resultMoney");
        log.info("ResultMoney {}", resultMoney);
	}

	public static String testMethod(String name, Integer age) {
		return "名称：" + name + ", 年龄：" + age;
	}

	public static Method getMethod(Class<?> classRef, String methodName, Class<?>... methodParm) {
		try {
			return classRef.getMethod(methodName, methodParm);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

}
