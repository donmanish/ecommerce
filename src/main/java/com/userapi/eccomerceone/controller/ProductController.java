package com.userapi.eccomerceone.controller;

import com.userapi.eccomerceone.dto.ErrorDto;
import com.userapi.eccomerceone.dto.ProductDTO;
import com.userapi.eccomerceone.exceptions.ProductNotFoundException;
import com.userapi.eccomerceone.exceptions.UnauthorizedException;
import com.userapi.eccomerceone.model.Category;
import com.userapi.eccomerceone.model.Product;
import com.userapi.eccomerceone.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Product APIs", description = "Operations for managing products")
public class ProductController {

   private  ProductService productService;

    //for for connection to database
    public ProductController(@Qualifier("ProductServiceImpl") ProductService productService) throws UnauthorizedException {
        this.productService = productService;
    }


    //get all products----------------------------------------------
    @GetMapping("/products")
    @Operation(summary = "List all products")
    public ResponseEntity<?> getAllProducts() throws UnauthorizedException{

        if (!isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated. Please login first.");
        }


        try {
            List<Product> products = productService.getAllProducts();
            List<ProductDTO> dtoList = products.stream()
                    .map(ProductDTO::new)
                    .toList();

            return ResponseEntity.ok().body(Map.of(
                    "message", "Products fetched successfully",
                    "data", dtoList
            ));

        } catch (Exception e) {
            e.printStackTrace(); // so you see what went wrong
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", "Failed to fetch products",
                            "error", e.getMessage()
                    ));
        }
    }



    //create a products
    @PostMapping("/products")
    @Operation(summary = "Create a new product")
    public ResponseEntity<Object> createProduct(@RequestBody Product product) throws UnauthorizedException{
        if (!isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated. Please login first.");
        }

        try {
            Product savedProduct = productService.createProduct(product);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Product created successfully");
            response.put("data", savedProduct);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to create product");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping("/products/{id}")

    public ResponseEntity<Object> getProduct(@PathVariable("id") Long productId) throws UnauthorizedException{
        if (!isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated. Please login first.");
        }

        try {
            Product product = productService.getSingleProduct(productId);

            // Map Product entity to DTO
            ProductDTO dto = new ProductDTO();
            dto.setId(product.getId());
            dto.setTitle(product.getTitle());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            dto.setImageUrl(product.getImageUrl());
            dto.setWeight(product.getWeight());
            dto.setHeight(product.getHeight());
            dto.setCategoryTitle(product.getCategory() != null ? product.getCategory().getTitle() : null);

            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Product fetched successfully");
            response.put("data", dto);

            return ResponseEntity.ok(response);

        } catch (ProductNotFoundException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Failed to fetch product");
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }





    //update product--------------------------------------------
    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable("id") Long productId,
                                                @RequestBody Product updateDetails) throws UnauthorizedException{

        if (!isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated. Please login first.");
        }

        try {
            Product updatedProduct = productService.updateProduct(productId, updateDetails);
            ProductDTO dto = new ProductDTO();
            dto.setId(updatedProduct.getId());
            dto.setTitle(updatedProduct.getTitle());
            dto.setDescription(updatedProduct.getDescription());
            dto.setPrice(updatedProduct.getPrice());
            dto.setImageUrl(updatedProduct.getImageUrl());
            dto.setWeight(updatedProduct.getWeight());
            dto.setHeight(updatedProduct.getHeight());
            dto.setCategoryTitle(updatedProduct.getCategory() != null ?
                    updatedProduct.getCategory().getTitle() : null);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Product updated successfully");
            response.put("data", dto);

            return ResponseEntity.ok(response);

        } catch (ProductNotFoundException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Failed to update product");
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //delete product--------------------------------------------------
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") Long productId) throws UnauthorizedException{

        if (!isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated. Please login first.");
        }

        try {
            Product deletedProduct = productService.deleteProduct(productId);

            // Map to DTO
            ProductDTO dto = new ProductDTO();
            dto.setId(deletedProduct.getId());
            dto.setTitle(deletedProduct.getTitle());
            dto.setDescription(deletedProduct.getDescription());
            dto.setPrice(deletedProduct.getPrice());
            dto.setImageUrl(deletedProduct.getImageUrl());
            dto.setWeight(deletedProduct.getWeight());
            dto.setHeight(deletedProduct.getHeight());
            dto.setCategoryTitle(deletedProduct.getCategory() != null ?
                    deletedProduct.getCategory().getTitle() : null);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Product deleted successfully");
            response.put("data", dto);

            return ResponseEntity.ok(response);

        } catch (ProductNotFoundException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Failed to delete product");
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    //all category--------------------------------------------------
    @GetMapping("/categories")
    public List<Category> getAllCategories() throws UnauthorizedException{
        if (!isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated. Please login first.");
        }

        return productService.getAllCategory();
    }

    //get specify category title--------------------------------------
    @GetMapping("/categories/{title}")
    public Category getCategory(@PathVariable("title") String title) throws UnauthorizedException{
        if (!isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated. Please login first.");
        }
        return productService.getCategoryByTitle(title);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFoundException(Exception e)
    {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(e.getMessage());

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    //backend aws connect work
    @GetMapping("/health")
    public ResponseEntity<String> checkHealthOfService()  throws UnauthorizedException{
        return new ResponseEntity<>("Backend service application work perfectly file", HttpStatus.OK);
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

}
