package cn.gson.oasys;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class OasysApplication {

	public static void main(String[] args) {
		SpringApplication.run(OasysApplication.class, args);
	}
}

