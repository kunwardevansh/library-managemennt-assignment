package librarymanagementsystem.assignment.entity;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "mails_sent")
@AllArgsConstructor
public class MailsSent extends IdBasedEntity {
    @OneToOne
    private User user;
    private String subject;
    private String body;
    private LocalDateTime sent_at;
    private String mail_type;
}

