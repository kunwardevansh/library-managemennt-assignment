package librarymanagementsystem.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import librarymanagementsystem.assignment.entity.Product;
import librarymanagementsystem.assignment.request.AddProductRequest;
import librarymanagementsystem.assignment.response.SuccessResponse;
import librarymanagementsystem.assignment.service.ProductService;

@RestController
@RequestMapping("api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<SuccessResponse> addProduct(@Valid @RequestBody AddProductRequest addProductRequest){
        return productService.addProduct(addProductRequest);
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id){
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateProduct(@RequestBody AddProductRequest addProductRequest){
        productService.updateProduct(addProductRequest);
        SuccessResponse response = new SuccessResponse("success","Product updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
