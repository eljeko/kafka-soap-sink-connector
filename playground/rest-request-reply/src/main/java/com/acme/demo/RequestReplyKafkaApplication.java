package com.acme.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan(basePackages = {
        "com.acme.config",
        "com.acme.consumer",
        "com.acme.controller",
        "com.acme.model"
})

@SpringBootApplication
public class RequestReplyKafkaApplication {
    @Configuration
    public class WebConfiguration implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**").allowedMethods("*");
        }
    }
    public static void main(String[] args) {
        SpringApplication.run(RequestReplyKafkaApplication.class, args);
    }
}
