package librarymanagementsystem.assignment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import librarymanagementsystem.assignment.entity.Category;
import librarymanagementsystem.assignment.exceptions.BadRequestException;
import librarymanagementsystem.assignment.exceptions.ResourceNotFoundException;
import librarymanagementsystem.assignment.repository.CategoryRepository;
import librarymanagementsystem.assignment.response.CategoryResponse;
import librarymanagementsystem.assignment.response.SuccessResponse;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        try{
            List<Category> categories = categoryRepository.findAll();
            List<CategoryResponse> response = categories.stream().map(this::convertToResponse).collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch categories",e);
        }
    }

    public ResponseEntity<Category> getCategory(Long category_id){
        try{
            Optional<Category> fetchCategory = categoryRepository.findById(category_id);
            System.out.println(fetchCategory);
            if(fetchCategory.isEmpty()) throw new ResourceNotFoundException("Category not found");
            Category category = fetchCategory.get();
            return new ResponseEntity<>(category,HttpStatus.OK);
        } catch(ResourceNotFoundException e){
            throw e;
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Not able to fetch category",e);
        }
    }

    public ResponseEntity<SuccessResponse> addCategory(String categoryName){
        SuccessResponse response = new SuccessResponse();
        try{
            addCategoryValidation(categoryName);
            Category newCategory = new Category();
            newCategory.setName(categoryName.toLowerCase());
            categoryRepository.save(newCategory);
            response.setMessage("Category added successfully");
            response.setStatus("success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to create category",e);
        }
    }

    private void addCategoryValidation(String categoryName){
        try{
            Optional<Category> category = categoryRepository.findByName(categoryName);
            if(category.isPresent()){
                throw new BadRequestException("Category already exists");
            }
        } catch(BadRequestException e){
            throw e;
        }
    }

    private CategoryResponse convertToResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setCategory_id(Long.valueOf(category.getId()));
        response.setName(category.getName());
        return response;
    }
}
