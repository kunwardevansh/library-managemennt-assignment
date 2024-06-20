package librarymanagementsystem.assignment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import librarymanagementsystem.assignment.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long>{
    @Query("SELECT od FROM OrderDetail od WHERE od.return_date IS NULL")
    Optional<List<OrderDetail>> findAllUnreturnedOrderDetails();
}
