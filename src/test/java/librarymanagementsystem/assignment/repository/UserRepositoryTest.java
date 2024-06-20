package librarymanagementsystem.assignment.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import librarymanagementsystem.assignment.entity.User;

@DataJpaTest
public class UserRepositoryTest {

    // @Autowired
    // private UserRepository userRepository;

    // @Test
    // public void findByEmail(String email){
    //     User newUser = new User();
    //     newUser.setEmail("test@email.com");
    //     userRepository.save(newUser);

    //     Optional<User> foundUser = userRepository.findByEmail("test@email.com");

    //     assertTrue(foundUser.isPresent());
    //     assertEquals(newUser.getEmail(), foundUser.get().getEmail());
    // }
    
}
