package com.login_ms.login_ms.docs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI loginMsOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("BankApp - Login Service API")
                .version("v1")
                .description("Microservico responsavel por cadastro, autenticacao e emissao de JWT.")
                .contact(new Contact().name("Victor Fernandes").email("contato@bankapp.local")));
    }
}
