package librarymanagementsystem.assignment.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import librarymanagementsystem.assignment.entity.OrderDetail;
import librarymanagementsystem.assignment.entity.User;
import librarymanagementsystem.assignment.request.AddUserRequest;
import librarymanagementsystem.assignment.request.AuthenticationRequest;
import librarymanagementsystem.assignment.response.AuthenticationResponse;
import librarymanagementsystem.assignment.service.AuthenticationService;
import librarymanagementsystem.assignment.service.ReminderService;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ReminderService reminderService;
    
    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody AddUserRequest addUserRequest){
        return new ResponseEntity<>(authenticationService.registerUser(addUserRequest), HttpStatus.OK);
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest){
        return new ResponseEntity<>(authenticationService.authenticate(authenticationRequest), HttpStatus.OK);
    }

    @GetMapping("mail")
    public void testEmail(){
        List<OrderDetail> overdueOrders = new ArrayList<>();
        List<OrderDetail> dueSoonOrders = new ArrayList<>();
        User user = new User();
        reminderService.remindUser(user,overdueOrders,dueSoonOrders);
        return;
        
    }
}
