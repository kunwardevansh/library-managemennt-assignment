package librarymanagementsystem.assignment.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import librarymanagementsystem.assignment.entity.Category;
import librarymanagementsystem.assignment.request.AddCategoryRequest;
import librarymanagementsystem.assignment.response.CategoryResponse;
import librarymanagementsystem.assignment.response.SuccessResponse;
import librarymanagementsystem.assignment.service.CategoryService;

@RestController
@RequestMapping("api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    
    @PostMapping
    public ResponseEntity<SuccessResponse> addCategory(@Valid @RequestBody AddCategoryRequest addCategoryRequest){
        return categoryService.addCategory(addCategoryRequest.getCategory_name());
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") Long id){
        return categoryService.getCategory(id);
    }
}
