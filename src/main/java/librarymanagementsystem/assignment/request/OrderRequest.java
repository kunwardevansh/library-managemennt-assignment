package librarymanagementsystem.assignment.request;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderRequest {
    @Size(min = 1, message = "The product list must contain at least one product.")
    private List<IdRequest> product_list = new ArrayList<>();
}
