package com.rh.rh_capsule;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		servers = {
				@Server(url = "http://dhxxn-sever.ddns.net", description = "배포 서버"),
				@Server(url = "http://localhost:8080", description = "로컬 개발 서버")
		}
)
@SpringBootApplication
public class RhCapsuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(RhCapsuleApplication.class, args);
	}

}
