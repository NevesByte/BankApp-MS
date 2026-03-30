package com.email_ms.email_sender_ms;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class EmailSenderMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailSenderMsApplication.class, args);
	}

}
