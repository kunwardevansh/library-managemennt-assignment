package librarymanagementsystem.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import librarymanagementsystem.assignment.entity.MailsSent;

@Repository
public interface MailsSentRepository extends JpaRepository<MailsSent,Long>{
}
