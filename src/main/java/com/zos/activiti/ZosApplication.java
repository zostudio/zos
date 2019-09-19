package com.zos.activiti;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@EnableSwagger2
@SpringBootApplication
public class ZosApplication {

	public static void main(String[] args) {
//		SpringApplication.run(ZosApplication.class, args);
		ConfigurableApplicationContext configurableApplicationContext = new SpringApplicationBuilder()
			.sources(ZosApplication.class)
			.web(WebApplicationType.SERVLET)
			.run(args);
		log.info("Init Beans");
		for (String beanDefinitionName : configurableApplicationContext.getBeanDefinitionNames()) {
			log.info("{}", beanDefinitionName);
		}
	}

}
