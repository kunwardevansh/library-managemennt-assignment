package librarymanagementsystem.assignment.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import librarymanagementsystem.assignment.entity.Order;
import librarymanagementsystem.assignment.request.OrderRequest;
import librarymanagementsystem.assignment.request.ReturnOrderRequest;
import librarymanagementsystem.assignment.response.SuccessResponse;
import librarymanagementsystem.assignment.service.OrderService;

@RestController
@RequestMapping("api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<SuccessResponse> createOrder(Principal connectedUser, @RequestBody OrderRequest orderRequest){
        return orderService.createOrder(connectedUser, orderRequest);
    }

    @GetMapping("{order_id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("order_id") Long order_id){
        return new ResponseEntity<>(orderService.getOrderById(order_id), HttpStatus.OK);
    }

    @PostMapping("return")
    public ResponseEntity<SuccessResponse> returnItems(Principal connectedUser, @RequestBody ReturnOrderRequest returnOrderRequest){
        return orderService.returnItems(connectedUser, returnOrderRequest);
    }
}
