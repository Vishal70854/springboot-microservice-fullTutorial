package com.programmingtechie.order_service.controller;

import com.programmingtechie.order_service.dto.OrderRequest;
import com.programmingtechie.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor    // created constructor dependency injection for all final fields at application start
public class OrderController {

    private final OrderService orderService;

    // since placeOrder() method calls internally inventory service using WebClient thats why we are implementing CircuitBreaker pattern in placeOrder() method
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // on success of order creation the response status should be created or 201 or 203
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod") // this will apply circuit breaker pattern in placeorder() method.
    @TimeLimiter(name = "inventory")    // this name should match the name in properties or yaml file and it will make an asynchronous call so return it as CompletableFuture
    @Retry(name = "inventory") // Retry in Resilience4j
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
    }

    // fall back method for circuit breaker
    // return type of fallback method should be same as the method which implements CircuitBreaker pattern, example mentioned above
    // also the parameter of fallbackMethod should be same as CircuitBreaker method
    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, Please order after sometime");
    }

}
