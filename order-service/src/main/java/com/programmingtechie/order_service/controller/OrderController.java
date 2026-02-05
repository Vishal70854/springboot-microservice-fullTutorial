package com.programmingtechie.order_service.controller;

import com.programmingtechie.order_service.dto.OrderRequest;
import com.programmingtechie.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor    // created constructor dependency injection for all final fields at application start
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // on success of order creation the response status should be created or 201 or 203
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        orderService.placeOrder(orderRequest);
        return "Order Placed Successfully";
    }

}
