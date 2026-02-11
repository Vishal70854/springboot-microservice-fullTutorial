package com.programmingtechie.order_service.service;

import com.programmingtechie.order_service.dto.InventoryResponse;
import com.programmingtechie.order_service.dto.OrderLineItemsDto;
import com.programmingtechie.order_service.dto.OrderRequest;
import com.programmingtechie.order_service.model.Order;
import com.programmingtechie.order_service.model.OrderLineItems;
import com.programmingtechie.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor    // created constructor dependency injection for all final fields at application start
@Transactional(readOnly = true) // transactional annotation used bcoz if all the execution of the method is okay, then only save the Inventory object in DB.. That means: ALL methods in this class run in a read-only transaction
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClient;  // as mentioned @RequiredArgsConstructor this field will be injected as it is declared  final as well

    @Transactional // <-- overrides readOnly=true // now we will be able to do write operation to db and save our data in db
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(orderLineItemsDto -> mapToDto(orderLineItemsDto))
                .collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItems);

        // get all skuCodes from OrderLineItemsList
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .collect(Collectors.toList());

        // Call Inventory Service, and place order if product is in stock
        InventoryResponse[] inventoryResponseArray = webClient.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                .retrieve() // this method will retrieve the response from inventory-service
                .bodyToMono(InventoryResponse[].class) // we defined the type of reponse we will get
                .block();// by default webClient makes asynchronous request, so block() will convert request to synchronous request()

        // check if all prodcuts are in stock then only save the order to db
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(e -> e.isInStock());

        if (allProductsInStock) {
            orderRepository.save(order);    // save order object to db
            return "Order Placed Successfully!!";
        }else{
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        return orderLineItems;
    }
}

