package org.iitbact.cc;

import java.util.Collections;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class CovidControlApplication {

	@Autowired
	DataSource dataSource;
	
	@Value("${swagger.host}")
	private String swaggerHost;

	public static void main(String[] args) {
		SpringApplication.run(CovidControlApplication.class, args);
	}

	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("org.iitbact.cc")).build().apiInfo(apiInfo())
				.host(swaggerHost);
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Admin Dashboard Developer API", "API documentation", "1.0", null,
				new Contact("Vinod Talapa", "www.linkedin.com/in/vinodtalapa", "vinodtalapa002.gmail.com"), null, null,
				Collections.emptyList());
	}

}
