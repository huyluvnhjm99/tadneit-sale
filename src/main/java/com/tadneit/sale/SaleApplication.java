package com.tadneit.sale;

import com.tadneit.sale.repository.impl.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableJpaAuditing
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@EnableJpaRepositories(
        basePackages = "com.tadneit.sale.repository",
        repositoryBaseClass = BaseRepositoryImpl.class
)
public class SaleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaleApplication.class, args);
    }

}
