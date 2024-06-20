package librarymanagementsystem.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import librarymanagementsystem.assignment.entity.OrderDetail;
import librarymanagementsystem.assignment.service.OrderDetailService;

@RestController
@RequestMapping("api/order_details")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;
    
    @GetMapping("{id}")
    public OrderDetail getOrderDetail(@PathVariable("id") Long id){
        return orderDetailService.getOrderDetail(id);
    }
}
