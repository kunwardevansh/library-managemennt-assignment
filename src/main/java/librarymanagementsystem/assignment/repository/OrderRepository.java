package librarymanagementsystem.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import librarymanagementsystem.assignment.entity.Order;

public interface OrderRepository extends JpaRepository<Order,Long>{

}
