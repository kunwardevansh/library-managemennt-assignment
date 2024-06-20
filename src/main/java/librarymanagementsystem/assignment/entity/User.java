package librarymanagementsystem.assignment.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User extends IdBasedEntity implements UserDetails{

    @Column(unique = true)
	private String email;

    private String first_name;
	private String last_name;
    private String password;
    private String phone_number;

    @NotEmpty(message = "Membership start date cannot be empty")
    private LocalDate membership_start_date;

    @NotEmpty(message = "Membership end date cannot be empty")
    private LocalDate membership_end_date;
    
    @NotEmpty(message = "Status cannot be empty")
    private String status; 
    // 0 -> blocked
    // 1 -> account created
    // 2 -> email verified 

    private LocalDate last_password_set_date;

    // @Enumerated(EnumType.STRING)
    // private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
        //     if (last_password_set_date == null) {
    //         return false;
    //     }
    //     LocalDate today = LocalDate.now();
    //     Period period = Period.between(last_password_set_date, today);
    //     return period.getMonths() < 3;
    }

    @Override
    public boolean isEnabled() {
        return true;
        // return (!status.equals("0"));
    }
}