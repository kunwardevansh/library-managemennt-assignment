package librarymanagementsystem.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import librarymanagementsystem.assignment.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

}
