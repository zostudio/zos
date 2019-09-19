package com.zos.generate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableJpaAuditing
@SpringBootApplication
@EnableTransactionManagement(mode= AdviceMode.PROXY, proxyTargetClass=true)
public class ZosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZosApplication.class, args);
//		ConfigurableApplicationContext configurableApplicationContext = new SpringApplicationBuilder()
//			.sources(ZosApplication.class)
//			.web(WebApplicationType.SERVLET)
//			.run(args);
//		log.info("Init Beans");
//		for (String beanDefinitionName : configurableApplicationContext.getBeanDefinitionNames()) {
//			log.info("{}", beanDefinitionName);
//		}
	}

}
