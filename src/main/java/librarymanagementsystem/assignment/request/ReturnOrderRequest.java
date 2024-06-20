package librarymanagementsystem.assignment.request;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReturnOrderRequest {
    @NotEmpty
    @Size(min = 1, message = "The order list must contain at least one order.")
    private List<IdRequest> order_detail_id = new ArrayList<>();
}
