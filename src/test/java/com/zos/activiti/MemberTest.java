package com.zos.activiti;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import com.zos.activiti.drools.Member;
import com.zos.activiti.drools.Sale;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberTest {
	
	@Autowired
	private KnowledgeBuilder knowledgeBuilder;
	
	@SuppressWarnings("unchecked")
	@Test
	public void contextLoads() {
		// 添加规则到 KnowledgeBuilder
		knowledgeBuilder.add(ResourceFactory.newClassPathResource("drools/discount.drl", MemberTest.class), ResourceType.DRL);
		// 获取知识规则包
		Collection<KnowledgePackage> kpackages = knowledgeBuilder.getKnowledgePackages();
		// 创建 KnowledgeBase 实例
		KnowledgeBase kbase = knowledgeBuilder.newKnowledgeBase();
		// 将知识规则包添加到 KnowledgeBase 中
		kbase.addKnowledgePackages(kpackages);
		// 使用 KnowledgeBase 实例, 创建 StatefulKnowledgeSession 实例
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		KnowledgeRuntimeLogger knowledgeRuntimeLogger = KnowledgeRuntimeLoggerFactory
				.newFileLogger(ksession, "log/drools");
		ksession.setGlobal("maxThen30", new ArrayList<Member>());
		// 定义一个事实对象
		Member memeber = new Member();
		memeber.setIdentity("gold");
		memeber.setAmount(130);
		memeber.setAge(31);
		// 向 StatefulKnowledgeSession 中加入事实
		ksession.insert(memeber);
		Sale sale = new Sale();
		sale.setAmount(50);
		sale.setDate(new Date());
		sale.setPrice(3);
		sale.setSaleCode("SALE2019");
		// 向 StatefulKnowledgeSession 中加入事实
		ksession.insert(sale);
		// 规则匹配
		ksession.fireAllRules();
		List<Member> members = (List<Member>) ksession.getGlobal("maxThen30");
		if (!CollectionUtils.isEmpty(members)) {
			for (Member member : members) {
				log.info("Member {} {} {}", member.getIdentity(), member.getAge(), member.getAfterDiscount());
			}
		}
		// 关闭 StatefulKnowledgeSession 的资源
		knowledgeRuntimeLogger.close();
		ksession.dispose();
	}

}
