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
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "verification_tokens")
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken extends IdBasedEntity {
    @OneToOne
    private User user;
    private String token;
    private LocalDateTime expiry_time;
}

