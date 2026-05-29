package com.devapix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ApiCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCatalogServiceApplication.class, args);
	}

}
