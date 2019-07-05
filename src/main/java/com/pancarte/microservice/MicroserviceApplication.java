package com.pancarte.microservice;

import com.pancarte.microservice.controller.EmailController;
import com.pancarte.microservice.controller.LibraryControleur;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.ContextConfiguration;


@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableScheduling
public class MicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceApplication.class, args);

	}

}

//		java -jar C:\Users\vivi\Desktop\x1\COURS\BTS\Java\microservice\target\microservice-0.0.1-SNAPSHOT.jar
