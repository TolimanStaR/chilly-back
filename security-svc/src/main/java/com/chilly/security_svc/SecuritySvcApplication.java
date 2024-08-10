package com.chilly.security_svc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class SecuritySvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecuritySvcApplication.class, args);
	}

}
