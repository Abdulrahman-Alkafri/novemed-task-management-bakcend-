package com.example.taskapp.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Management API")
                        .version("1.0.0")
                        .description("API documentation for the Task Management System. All endpoints are grouped and described for easy use.")
                        .contact(
                                new Contact()
                                        .email("abdalrhman.alkafry@gmail.com")
                                        .name("Abdulrahman Alkafri")
                        )
                        .summary("Novamed Task Assignment Backend Api Documentation"));
    }
} 