package com.userapi.eccomerceone;

import com.userapi.eccomerceone.model.Product;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProjectMainClass {

    public static void main(String[] args) {
        Product p = new Product();
        SpringApplication.run(ProjectMainClass.class, args);
    }

}
