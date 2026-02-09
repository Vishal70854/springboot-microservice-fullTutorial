package com.programmingtechie.order_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced   // this is used for load balancing request between multiple instances of any service being called from this order-service
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }

}
