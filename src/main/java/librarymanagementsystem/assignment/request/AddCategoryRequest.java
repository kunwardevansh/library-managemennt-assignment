package librarymanagementsystem.assignment.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddCategoryRequest {
    @NotEmpty
    @Size(min = 2, message = "Category name should have at least 2 characters")
    private String category_name;
}
