package librarymanagementsystem.assignment.service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import librarymanagementsystem.assignment.entity.Role;
import librarymanagementsystem.assignment.entity.User;
import librarymanagementsystem.assignment.entity.VerificationToken;
import librarymanagementsystem.assignment.exceptions.BadRequestException;
import librarymanagementsystem.assignment.exceptions.ResourceNotFoundException;
import librarymanagementsystem.assignment.model.Mail;
import librarymanagementsystem.assignment.repository.UserRepository;
import librarymanagementsystem.assignment.repository.VerificationTokenRepository;
import librarymanagementsystem.assignment.request.AddUserRequest;
import librarymanagementsystem.assignment.response.SuccessResponse;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<SuccessResponse> addUser(AddUserRequest addUserRequest){
        try{
            User user = addUserRequest.toEntity(addUserRequest);
            LocalDate today = LocalDate.now();
            user.setMembership_start_date(today);
            user.setMembership_end_date(today.plusMonths(6));
            user.setStatus("1");
            user.setLast_password_set_date(today);
            userRepository.save(user);
            SuccessResponse response = new SuccessResponse();
            response.setMessage("Successfully created user");
            response.setStatus("success");
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to add user",e);
        }
    }

    public ResponseEntity<List<User>> getAllUser(){
        try{
            return new ResponseEntity<>(userRepository.findAll(),HttpStatus.OK);
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch users",e);
        }
    }

    public User getUserById(Long user_id){
        try{
            Optional<User> user =  userRepository.findById(user_id);
            if(user.isEmpty()) throw new ResourceNotFoundException("User not found");
            return user.get();
        } catch(ResourceNotFoundException e){
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch user",e);
        }
    }
    
    public User getUserByEmail(String email){
        try{
            Optional<User> user =  userRepository.findByEmail(email);
            if(user.isEmpty()) throw new ResourceNotFoundException("User not found");
            return user.get();
        } catch(ResourceNotFoundException e){
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch user",e);
        }
    }

    public void sendVerificationEmail(Principal connectedUser){
        try{
            var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            String token = UUID.randomUUID().toString();
            VerificationToken verificationToken = new VerificationToken(user, token, LocalDateTime.now().plusHours(12));
            verificationTokenRepository.save(verificationToken);
            String subject = "Verify email";
            StringBuilder message = new StringBuilder("Dear user,\n\n <br>"); 
            message.append("Thanks for creating account at our Library, please click on the link below to verify the email.<br><br>");
            message.append("http://localhost:8080/api/user/verify/email/" + token);
            message.append("<br>Thanks");
            
            Mail mail = new Mail();
            mail.setMailTo(user.getEmail());
            mail.setMailFrom("kunwardevansh@rocketmail.com");
            mail.setMailSubject(subject);
            mail.setMailContent(message.toString());
		    mailService.sendEmail(mail, user, "email_verification");

        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Not able to send verification email",e);
        }
    }

    public void verifyEmail(String token){
        try{
            Optional<VerificationToken> foundVerificationToken = verificationTokenRepository.findByToken(token);
            if(!foundVerificationToken.isPresent()) throw new BadRequestException("Verification link expired");
            VerificationToken verificationToken = foundVerificationToken.get();
            if(verificationToken.getExpiry_time().isBefore(LocalDateTime.now())){
                throw new BadRequestException("Verification link expired");
            }
            User user = verificationToken.getUser();
            user.setStatus("2");
            userRepository.save(user);
            verificationTokenRepository.delete(verificationToken);
        }   catch (BadRequestException e) {
            throw e;
        }  
        catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Not able to process verification email request",e);
        }
    }
    
    public void sendForgotPasswordLink(String userEmail){
        try{
            User user = getUserByEmail(userEmail);
            if(user.getStatus().equals("2")){
                throw new BadRequestException("User email already verified");
            }
            String token = UUID.randomUUID().toString();
            Optional<VerificationToken> foundVerificationToken =  verificationTokenRepository.findByUser(user);
            VerificationToken verificationToken;
            if(foundVerificationToken.isPresent()){
                verificationToken = foundVerificationToken.get();
                verificationToken.setExpiry_time(LocalDateTime.now().plusHours(12));
                verificationToken.setToken(token);
            } else{
                verificationToken = new VerificationToken(user, token, LocalDateTime.now().plusHours(12));
            }
            verificationTokenRepository.save(verificationToken);
            String subject = "Reset password";
            StringBuilder message = new StringBuilder("Dear user,\n\n <br>"); 
            message.append("Please click on the link below to reset your password.<br><br>");
            message.append("http://localhost:8080/api/user/reset/password/" + token);
            message.append("<br>Thanks");
            
            Mail mail = new Mail();
            mail.setMailTo(user.getEmail());
            mail.setMailFrom("kunwardevansh@rocketmail.com");
            mail.setMailSubject(subject);
            mail.setMailContent(message.toString());
		    mailService.sendEmail(mail, user, "reset_password");

        } catch(BadRequestException e){
            throw e;
        } catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void resetForgotPassword(String token, String newPassword){
        try{
            Optional<VerificationToken> foundVerificationToken = verificationTokenRepository.findByToken(token);
            if(!foundVerificationToken.isPresent()) throw new BadRequestException("Link is expired.");
            VerificationToken verificationToken = foundVerificationToken.get();
            if(verificationToken.getExpiry_time().isBefore(LocalDateTime.now())){
                throw new BadRequestException("Link is expired");
            }
            User user = verificationToken.getUser();
            user.setLast_password_set_date(LocalDate.now());
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            verificationTokenRepository.delete(verificationToken);
        } catch(Exception e){
            throw new RuntimeException("Not to process reset password request.");
        }
    }
}
