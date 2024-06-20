package librarymanagementsystem.assignment.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotEmpty(message="Password cannot be blank")
    private String password;
    @NotEmpty(message="Token cannot be blank")
    private String token;
}
