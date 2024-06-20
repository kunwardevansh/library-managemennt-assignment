package librarymanagementsystem.assignment.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import librarymanagementsystem.assignment.entity.User;
import librarymanagementsystem.assignment.repository.UserRepository;
import librarymanagementsystem.assignment.request.AddUserRequest;
import librarymanagementsystem.assignment.request.AuthenticationRequest;
import librarymanagementsystem.assignment.response.AuthenticationResponse;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthenticationResponse registerUser(AddUserRequest addUserRequest){
        addUserRequest.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));
        User user = addUserRequest.toEntity(addUserRequest);
        LocalDate today = LocalDate.now();
        user.setMembership_start_date(today);
        user.setMembership_end_date(today.plusMonths(6));
        user.setStatus("1");
        user.setLast_password_set_date(today);
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(jwtToken);
        return response;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );
        var user = userService.getUserByEmail(authenticationRequest.getEmail());
        var jwtToken = jwtService.generateToken(user);
        var response = new AuthenticationResponse(jwtToken);
        return response;
    }
}
