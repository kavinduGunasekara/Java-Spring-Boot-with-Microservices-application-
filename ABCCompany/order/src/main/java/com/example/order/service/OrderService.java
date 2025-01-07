package com.example.order.service;


import com.example.inventory.dto.InventoryDTO;
import com.example.order.common.ErrorOrderResponse;
import com.example.order.common.OrderResponse;
import com.example.order.common.SuccessOrderResponse;
import com.example.order.dto.OrderDTO;
import com.example.order.model.Orders;
import com.example.order.repo.OrderRepo;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
@Transactional
public class OrderService {

    private final WebClient webClient;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ModelMapper modelMapper;

    public OrderService(WebClient.Builder webClientBuilder , OrderRepo orderRepo, ModelMapper modelMapper) {

        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/api/v1").build();
        this.orderRepo = orderRepo;
        this.modelMapper = modelMapper;
    }


    public List<OrderDTO> getAllOrders() {
        List<Orders>orderList = orderRepo.findAll();
        return modelMapper.map(orderList, new TypeToken<List<OrderDTO>>(){}.getType());
    }
    public OrderResponse saveOrder(OrderDTO OrderDTO){

        Integer itemId = OrderDTO.getItemId();
        try {
               InventoryDTO inventoryResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/item/{itemId}").build(itemId))
                    .retrieve()
                    .bodyToMono(InventoryDTO.class)
                    .block();

                assert  inventoryResponse!= null;

            System.out.println(inventoryResponse);

               if (inventoryResponse.getQuantity()> 0){
                   orderRepo.save(modelMapper.map(OrderDTO,Orders.class));
                   return new SuccessOrderResponse(OrderDTO);
               }
               else{
                   return new ErrorOrderResponse("Item not available, please try later");
               }
        }
        catch(Exception e){
            e.printStackTrace();

        }
        return null;

    }

    public OrderDTO updateOrder(OrderDTO OrderDTO) {
        orderRepo.save(modelMapper.map(OrderDTO, Orders.class));
        return OrderDTO;
    }

    public String deleteOrder(Integer orderId) {
        orderRepo.deleteById(orderId);
        return "Order deleted";
    }

    public OrderDTO getOrderById(Integer orderId) {
        Orders order = orderRepo.getOrderById(orderId);
        return modelMapper.map(order, OrderDTO.class);
    }
}
