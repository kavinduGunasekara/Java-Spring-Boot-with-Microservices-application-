package com.example.order.controller;


//import com.base.base.dto.OrderEventDTO;
//import com.example.order.common.OrderResponse;
import com.example.order.common.OrderResponse;
import com.example.order.dto.OrderDTO;
//import com.example.order.kafka.OrderProducer;
//import com.example.order.service.OrderService;
import com.example.order.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/")
public class OrderController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OrderService orderService;


    @GetMapping("/getorders")
    public List<OrderDTO> getOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/order/{orderId}")
    public OrderDTO getOrderById(@PathVariable Integer orderId) {
        return orderService.getOrderById(orderId);
    }

    @PostMapping("/addorder")
    public OrderResponse saveOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.saveOrder(orderDTO);
    }

    @PutMapping("/updateorder")
    public OrderDTO updateOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.updateOrder(orderDTO);
    }

    @DeleteMapping("/deleteorder/{orderId}")
    public String deleteOrder(@PathVariable Integer orderId) {
        return orderService.deleteOrder(orderId);
    }
}
