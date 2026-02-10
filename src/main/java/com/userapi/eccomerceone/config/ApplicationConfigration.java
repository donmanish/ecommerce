package com.userapi.eccomerceone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfigration {
    @Bean
   public RestTemplate createRestTemplate(){
       return new RestTemplate();
   }
}
