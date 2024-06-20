package librarymanagementsystem.assignment.request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AddProductRequest {

    private Long id;

    @NotEmpty(message="Name cannot be blank")
	private String name;

    @NotEmpty(message="Image cannot be blank")
	private String image;

    @NotEmpty(message="Category cannot be blank")
    private Long category_id;

    
    private Long stock;

    private Long issue_duration;

    private Double fine_per_day;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AddProductDetailRequest> productDetails = new ArrayList<>();
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;
}
