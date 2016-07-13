package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@EnableZuulProxy //Proxy load balancing server
@EnableBinding(Source.class) //Added rabbitMQ message channel
@EnableCircuitBreaker //Hystrix circuit breaker enablement
@EnableDiscoveryClient//register to service discovery Eureka
@SpringBootApplication
public class ReservationClientApplication {

    @Bean
    AlwaysSampler alwaysSampler(){
        return new AlwaysSampler();
    }

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ReservationClientApplication.class, args);
	}

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        RestTemplate rt = new RestTemplate();
        return rt;
    }
}