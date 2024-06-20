package librarymanagementsystem.assignment.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class IdRequest {
    @NotEmpty(message="Id cannot be blank")
    private Long id;
}
