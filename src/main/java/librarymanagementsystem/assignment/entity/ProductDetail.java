package librarymanagementsystem.assignment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "product_details")
public class ProductDetail extends IdBasedEntity {
    
	private String name;
	private String value;

	@ManyToOne
	@JoinColumn(name = "product_id")
    @JsonBackReference
	private Product product;
}
