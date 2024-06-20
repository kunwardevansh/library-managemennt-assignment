package librarymanagementsystem.assignment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import librarymanagementsystem.assignment.entity.Category;
import librarymanagementsystem.assignment.entity.Product;
import librarymanagementsystem.assignment.entity.ProductDetail;
import librarymanagementsystem.assignment.exceptions.ResourceNotFoundException;
import librarymanagementsystem.assignment.repository.CategoryRepository;
import librarymanagementsystem.assignment.repository.ProductDetailRepository;
import librarymanagementsystem.assignment.repository.ProductRepository;
import librarymanagementsystem.assignment.request.AddProductRequest;
import librarymanagementsystem.assignment.response.SuccessResponse;

@Service
public class ProductService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    public Product getProductById(Long product_id){
        try{
            Optional<Product> fetchProduct = productRepository.findById(product_id);
            if(fetchProduct.isEmpty()) throw new ResourceNotFoundException("Product not found");
            return fetchProduct.get();
        } catch(ResourceNotFoundException e){
            throw e;
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Not able to fetch product",e);
            
        }
    }

    public List<Product> getAllProducts(){
        try{
            return productRepository.findAll();
        } catch (Exception e){
            throw new RuntimeException("Not able to fetch product list",e);
        }
    }

    @Transactional()
    public ResponseEntity<SuccessResponse> addProduct(AddProductRequest addProductRequest) {
        SuccessResponse response = new SuccessResponse();
        try{
            Optional<Category> category =  categoryRepository.findById(addProductRequest.getCategory_id());
            if(category.isEmpty()) throw new ResourceNotFoundException("Category not found");
            Category productCategory = category.get();
            Product newProduct = new Product();
            newProduct.setName(addProductRequest.getName());
            newProduct.setCategory(productCategory);
            newProduct.setImage(addProductRequest.getImage());
            newProduct.setStock(addProductRequest.getStock());

            addProductRequest.getProductDetails().forEach((request) -> {
                ProductDetail productDetail = new ProductDetail();
                System.out.println(request.getName() + " " + request.getValue());
                productDetail.setName(request.getName());
                productDetail.setValue(request.getValue());
                productDetail.setProduct(newProduct);
                productDetailRepository.save(productDetail);
                newProduct.getDetails().add(productDetail);
            });

            productCategory.setProductList(newProduct);
            productRepository.save(newProduct);

            response.setMessage("Product added successfully");
            response.setStatus("success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(ResourceNotFoundException e){
            throw e;
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to add product",e);
        }
    }

        @Transactional
        public ResponseEntity<SuccessResponse> updateProduct(AddProductRequest addProductRequest) {
        SuccessResponse response = new SuccessResponse();
        try {
            Optional<Product> optionalProduct = productRepository.findById(addProductRequest.getId());
            if (optionalProduct.isEmpty()) throw new ResourceNotFoundException("Product not found");

            Product product = optionalProduct.get();
            if(addProductRequest.getName() != null){
                product.setName(addProductRequest.getName());
            } 
            if(addProductRequest.getImage() != null){
                product.setImage(addProductRequest.getImage());
            } 
            if(addProductRequest.getStock() != null){
                product.setStock(addProductRequest.getStock());
            }
            if(addProductRequest.getIssue_duration() != null){
                product.setIssue_duration(addProductRequest.getIssue_duration());
            }
            if(addProductRequest.getFine_per_day() != null){
                product.setFine_per_day(addProductRequest.getFine_per_day());
            }

            product.getDetails().clear();
            addProductRequest.getProductDetails().forEach(detailRequest -> {
                ProductDetail productDetail = new ProductDetail();
                productDetail.setName(detailRequest.getName());
                productDetail.setValue(detailRequest.getValue());
                productDetail.setProduct(product);
                productDetailRepository.save(productDetail);
                product.getDetails().add(productDetail);
            });

            productRepository.save(product);

            response.setMessage("Product updated successfully");
            response.setStatus("success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update product",e);
        }
    }
}
