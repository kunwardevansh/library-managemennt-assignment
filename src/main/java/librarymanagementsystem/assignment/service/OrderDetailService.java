package librarymanagementsystem.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import librarymanagementsystem.assignment.entity.OrderDetail;
import librarymanagementsystem.assignment.exceptions.ResourceNotFoundException;
import librarymanagementsystem.assignment.repository.OrderDetailRepository;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public OrderDetail getOrderDetail(Long id){
        return orderDetailRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("Order detail not found"));
    }
}
