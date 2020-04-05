package edu.application.licenses;

import com.fasterxml.jackson.databind.Module;
import edu.application.licenses.utils.TransactionContextInterceptor;
import io.vavr.jackson.datatype.VavrModule;
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
        return restTemplateBuilder.setConnectTimeout(Duration.of(1, ChronoUnit.HOURS))
                                  .setReadTimeout(Duration.of(1, ChronoUnit.HOURS))
                                  .additionalInterceptors(new TransactionContextInterceptor())
                                  .build();
    }

    @Bean
    Module vavrModule() {
        return new VavrModule();
    }

    public static void main(String[] args) {
        SpringApplication.run(LicensingServiceApplication.class, args);
    }
}
