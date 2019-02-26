package com.simple.pay.service.message;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages={"com.simple.pay.service.message"})
@MapperScan("com.simple.pay.service.message.dao")
public class PayServiceMessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayServiceMessageApplication.class, args);
	}
}
