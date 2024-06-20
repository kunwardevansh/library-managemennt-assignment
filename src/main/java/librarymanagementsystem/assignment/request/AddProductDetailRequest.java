package librarymanagementsystem.assignment.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddProductDetailRequest {
    @NotEmpty
    @Size(min = 2, message = "Product detail name should have at least 2 characters")
    private String name;
    @NotEmpty
    @Size(min = 2, message = "Product detail value should have at least 2 characters")
    private String value;
}
