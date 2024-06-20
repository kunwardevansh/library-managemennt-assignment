package librarymanagementsystem.assignment.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import librarymanagementsystem.assignment.entity.Category;

@DataJpaTest
public class CategoryRepositoryTest {
    
    // @Autowired
    // private CategoryRepository categoryRepository;

    // @Test
    // void findByName(String categoryName){
    //     Category category = new Category("Vehicle");
    //     categoryRepository.save(category);

    //     Optional<Category> foundCategory = categoryRepository.findByName("Vehicle");
    //     assertTrue(foundCategory.isPresent());
    //     assertEquals("Vehicle", foundCategory.get().getName());
    // }
}
