package com.study.contactapi.docs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI customOpenAPI() {
    Server server = new Server();

    server.setUrl("http://localhost:8080");
    server.description("Contact API server");

		return new OpenAPI().info(
      new Info()
        .title("Contact API Docs")
        .description("Contact API routes documentation")
        .version("1.0"))
        .servers(List.of(server));
	}
}