package librarymanagementsystem.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import librarymanagementsystem.assignment.entity.Fine;

@Repository
public interface FineRepository extends JpaRepository<Fine,Long>{
    
}
