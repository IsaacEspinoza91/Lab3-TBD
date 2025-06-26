package com.tbd.DeliveryMedicamentos.config;

import org.sql2o.Sql2o;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Sql2oConfig {

    @Bean
    public Sql2o sql2o() {
        return new Sql2o(
                "jdbc:postgresql://localhost:5432/delivery_medicamentos",
                "postgres",
                "postgres"
        );
    }
}

