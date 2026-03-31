package com.painel_bank_ms.painel_ms.geral_configurations.docs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI painelMsOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("BankApp - Painel Service API")
                .version("v1")
                .description("Microservico de operacoes financeiras: conta, emprestimos e transferencias.")
                .contact(new Contact().name("Victor Fernandes").email("contato@bankapp.local")));
    }
}
