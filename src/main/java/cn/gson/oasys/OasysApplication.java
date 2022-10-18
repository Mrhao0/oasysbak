package cn.gson.oasys;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "cn.gson.oasys.model.dao",
		entityManagerFactoryRef = "entityManagerFactory",
		transactionManagerRef = "transactionManager")
@EntityScan("cn.gson.oasys.model.entity")
public class OasysApplication {
	public static void main(String[] args) {
		SpringApplication.run(OasysApplication.class, args);
	}
}

