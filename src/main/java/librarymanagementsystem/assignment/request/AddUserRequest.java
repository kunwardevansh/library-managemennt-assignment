package librarymanagementsystem.assignment.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import librarymanagementsystem.assignment.entity.Role;
import librarymanagementsystem.assignment.entity.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AddUserRequest {

    private String email;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;

    public User toEntity(AddUserRequest addUserRequest){
        User user = new User();
        user.setEmail(addUserRequest.getEmail());
        user.setFirst_name(addUserRequest.getFirst_name());
        user.setLast_name(addUserRequest.getLast_name());
        user.setPhone_number(addUserRequest.getPhone_number());
        user.setPassword(addUserRequest.getPassword());
        // user.setRole(Role.USER);
        return user;
    }
}