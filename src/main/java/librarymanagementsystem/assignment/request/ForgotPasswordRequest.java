package librarymanagementsystem.assignment.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @NotEmpty(message="Email cannot be blank")
    private String email;
}
