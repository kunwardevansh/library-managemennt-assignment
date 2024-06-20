package librarymanagementsystem.assignment.service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import librarymanagementsystem.assignment.entity.Fine;
import librarymanagementsystem.assignment.entity.Order;
import librarymanagementsystem.assignment.entity.OrderDetail;
import librarymanagementsystem.assignment.entity.Product;
import librarymanagementsystem.assignment.entity.ProductDetail;
import librarymanagementsystem.assignment.entity.User;
import librarymanagementsystem.assignment.exceptions.BadRequestException;
import librarymanagementsystem.assignment.exceptions.ResourceNotFoundException;
import librarymanagementsystem.assignment.repository.FineRepository;
import librarymanagementsystem.assignment.repository.OrderDetailRepository;
import librarymanagementsystem.assignment.repository.OrderRepository;
import librarymanagementsystem.assignment.repository.ProductRepository;
import librarymanagementsystem.assignment.request.AddProductRequest;
import librarymanagementsystem.assignment.request.OrderRequest;
import librarymanagementsystem.assignment.request.ReturnOrderRequest;
import librarymanagementsystem.assignment.request.IdRequest;
import librarymanagementsystem.assignment.response.SuccessResponse;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FineRepository fineRepository;

    @Transactional
    public ResponseEntity<SuccessResponse> createOrder(Principal connectedUser, OrderRequest orderRequest){
        SuccessResponse response = new SuccessResponse();
        try{
            var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            createOrderValidation(orderRequest, user);
            LocalDateTime today = LocalDateTime.now();
            Order order = new Order();
            order.setUser(user);
            order.setOrder_date(today);
            for (IdRequest productIdRequest : orderRequest.getProduct_list()) {
                OrderDetail orderDetail = new OrderDetail();
                Long productId = productIdRequest.getId();
                Product product = productService.getProductById(productId);
                
                orderDetail.setOrder(order);
                orderDetail.setProduct(product);
                
                if(product.getStock() > 0){
                    orderDetail.setDue_date(today.plusDays(14));
                    orderDetail.setStatus("placed");
                    product.setStock(product.getStock() - 1);
                    productRepository.save(product);
                } else{
                    orderDetail.setStatus("out_of_stock");
                }
                order.setOrderDetailsList(orderDetail);
            }
            System.out.println("Line 7");
            orderRepository.save(order);
            System.out.println("Line 8");
            response.setMessage("Order successfull");
            response.setStatus("success");
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch(BadRequestException e){
            throw e;
        } catch(Exception e){
            response.setMessage("Failed to place order");
            response.setStatus("failure");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Order getOrderById(Long order_id){
        try{
            Optional<Order> order = orderRepository.findById(order_id);
            if(order.isEmpty()) throw new ResourceNotFoundException("Order not found");
            return order.get();
        } catch(ResourceNotFoundException e){
            throw e;
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch order");
        }
    }

    private void createOrderValidation(OrderRequest orderRequest, User user){
        try{
            if(!user.getStatus().equals("2")) throw new BadRequestException("User not active");
            LocalDate todayDate = LocalDate.now();
            if(user.getMembership_end_date().isBefore(todayDate)) throw new BadRequestException("User membership expired");
            return;
        } catch(BadRequestException e){
            e.printStackTrace();
            throw e;
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to create order");
        }
    }

    @Transactional
    public ResponseEntity<SuccessResponse> returnItems(Principal connectedUser, ReturnOrderRequest returnOrderRequest) {
        SuccessResponse response = new SuccessResponse();
        try {
            var authenticatedUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            for(IdRequest productIdRequest : returnOrderRequest.getOrder_detail_id()){
                OrderDetail orderDetail = orderDetailService.getOrderDetail(productIdRequest.getId());

                if (!orderDetail.getOrder().getUser().getId().equals(authenticatedUser.getId())) {
                    throw new AccessDeniedException("Unauthorized: Cannot return item(s) that do not belong to you");
                }

                if(orderDetail.getStatus().equals("returned")){
                    throw new BadRequestException("Item: " + orderDetail.getProduct().getName() + " already returned");
                }

                orderDetail.setReturn_date(LocalDateTime.now());
                orderDetail.setStatus("returned");

                Product product = orderDetail.getProduct();
                product.setStock(product.getStock() + 1);

                productRepository.save(product);

                if (orderDetail.getReturn_date().isAfter(orderDetail.getDue_date())) {
                    long daysLate = java.time.Duration.between(orderDetail.getDue_date(), orderDetail.getReturn_date()).toDays();
                    double fineAmount = daysLate * product.getFine_per_day();

                    Fine fine = new Fine();
                    fine.setAmount(fineAmount);
                    fineRepository.save(fine);
                    // orderDetail.setFine(fine);
                }
                orderDetailRepository.save(orderDetail);
            }
            response.setMessage("Item returned successfully");
            response.setStatus("success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(AccessDeniedException e){
            throw e;
        } catch(BadRequestException e){
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to return item");
        }
    }
}
