package com.rh.rh_capsule;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers = {@Server(url = "https://write-capsule.xyz", description = "Default Server URL")})
@SpringBootApplication
public class RhCapsuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(RhCapsuleApplication.class, args);
	}

}
