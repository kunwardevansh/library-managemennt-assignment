package librarymanagementsystem.assignment.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import librarymanagementsystem.assignment.entity.User;
import librarymanagementsystem.assignment.request.AddUserRequest;
import librarymanagementsystem.assignment.request.ForgotPasswordRequest;
import librarymanagementsystem.assignment.request.ResetPasswordRequest;
import librarymanagementsystem.assignment.response.SuccessResponse;
import librarymanagementsystem.assignment.service.UserService;

@RestController
@RequestMapping("api/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<SuccessResponse> addUser(@RequestBody AddUserRequest addUserRequest){
        return userService.addUser(addUserRequest);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return userService.getAllUser();
    }

    @GetMapping("{user_id}")
    public ResponseEntity<User> getUserById(@PathVariable("user_id") Long user_id){
        return new ResponseEntity<>(userService.getUserById(user_id), HttpStatus.OK);
    }

    @PostMapping("verify/email")
    public ResponseEntity<SuccessResponse> sendVerificationEmail(Principal connectedUser){
        SuccessResponse response = new SuccessResponse(
            "success",
            "Verification link sent to email address, please click on the link to verify email."
        );
        userService.sendVerificationEmail(connectedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("verify/email/{token}")
    public ResponseEntity<SuccessResponse> verifyEmail(@PathVariable("token") String token ){
        userService.verifyEmail(token);
        SuccessResponse response = new SuccessResponse(
            "success",
            "Email successfully verified."
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("forgot/password")
    public ResponseEntity<SuccessResponse> sendForgotPasswordEmail(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        userService.sendForgotPasswordLink(forgotPasswordRequest.getEmail());
        SuccessResponse response = new SuccessResponse(
            "success",
            "Link to reset password sent on your email id."
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("forgot/password")
    public ResponseEntity<SuccessResponse> resetForgotPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        userService.resetForgotPassword(resetPasswordRequest.getToken(), resetPasswordRequest.getPassword());
        SuccessResponse response = new SuccessResponse(
            "success",
            "Password changes successfully."
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
