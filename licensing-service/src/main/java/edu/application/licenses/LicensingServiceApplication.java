package edu.application.licenses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableEurekaClient
public class LicensingServiceApplication {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        //RestTemplate set to long timeout to test hystrix circuit breaking
        restTemplateBuilder.setConnectTimeout(Duration.of(1, ChronoUnit.HOURS));
        restTemplateBuilder.setReadTimeout(Duration.of(1, ChronoUnit.HOURS));
        return restTemplateBuilder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(LicensingServiceApplication.class, args);
    }
}
