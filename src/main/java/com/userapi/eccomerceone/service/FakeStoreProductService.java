package com.userapi.eccomerceone.service;

import com.userapi.eccomerceone.dto.FakeStoreProductDto;
import com.userapi.eccomerceone.exceptions.ProductNotFoundException;
import com.userapi.eccomerceone.model.Category;
import com.userapi.eccomerceone.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("fakeProductService")
public class FakeStoreProductService implements ProductService{

    private RestTemplate restTemplate;

    //create constructor
    public FakeStoreProductService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getSingleProduct(Long ProductId) throws ProductNotFoundException {
        //here we call our fakestore product service of perticular product
        FakeStoreProductDto fakeStoreProductDto = restTemplate.getForObject(
          "https://fakestoreapi.com/products/" + ProductId,
                FakeStoreProductDto.class
        );
//        ResponseEntity<FakeStoreProductDto> fakeStoreProductDto = restTemplate.getForEntity(
//                "https://fakestoreapi.com/products/" + ProductId,
//                FakeStoreProductDto.class
//        );
//
//        if(fakeStoreProductDto.getStatusCode() == HttpStatus.OK)
//        {
//            //ptint success of response
//        } else if(fakeStoreProductDto.getStatusCode() == HttpStatus.NOT_FOUND)
//        {
//            //handle thing
//        }
        if(fakeStoreProductDto == null)
        {
            throw new ProductNotFoundException("Product not found wit ID: " + ProductId);
        }
        return fakeStoreProductDto.toProduct();
    }

    @Override
    public List<Product> getAllProducts() {
        // Fetch the array of FakeStoreProductDto objects from the API
        FakeStoreProductDto[] fakeStoreProductDto = restTemplate.getForObject(
                "https://fakestoreapi.com/products",
                FakeStoreProductDto[].class
        );

        // Check if the response is not null to avoid NullPointerException
        if (fakeStoreProductDto == null) {
            return List.of();
        }

        // Convert the array of FakeStoreProductDto to a list of Product objects
        return Arrays.stream(fakeStoreProductDto)
                .map(FakeStoreProductDto::toProduct)
                .collect(Collectors.toList());
    }

    @Override
    public Product createProduct(Product product)
    {
        FakeStoreProductDto fs = new FakeStoreProductDto();
        fs.setId(product.getId());
        fs.setTitle(product.getTitle());
        fs.setCategory(product.getCategory().getTitle());
        fs.setImage(product.getImageUrl());
        fs.setDescription(product.getDescription());
        fs.setPrice(product.getPrice());

        FakeStoreProductDto fakeStoreProductDto = restTemplate.postForObject(
           "https://fakestoreapi.com/products",
            fs,
                FakeStoreProductDto.class
        );
        Product p = new Product();
        p.setId(fakeStoreProductDto.getId());

        return fakeStoreProductDto.toProduct();
    }

    @Override
    public Product updateProduct(Long ProductId, Product updateDetails) {
        // Fetch the existing product details from the API
        FakeStoreProductDto existingProductDto = restTemplate.getForObject(
                "https://fakestoreapi.com/products/" + ProductId,
                FakeStoreProductDto.class
        );

        // Check if the product exists
        if (existingProductDto == null) {
            return null;
        }

        // Update the product details with the new information
        existingProductDto.setTitle(updateDetails.getTitle());
        existingProductDto.setCategory(updateDetails.getCategory().getTitle());
        existingProductDto.setImage(updateDetails.getImageUrl());
        existingProductDto.setDescription(updateDetails.getDescription());
        existingProductDto.setPrice(updateDetails.getPrice());

        // Perform the PUT request to update the product on the API
        restTemplate.put(
                "https://fakestoreapi.com/products/" + ProductId,
                existingProductDto
        );

        // Convert and return the updated product
        return existingProductDto.toProduct();
    }

    @Override
    public Product deleteProduct(Long ProductId) {
        // Fetch the product details before deletion
        FakeStoreProductDto deleteProduct = restTemplate.getForObject(
                "https://fakestoreapi.com/products/" + ProductId,
                FakeStoreProductDto.class
        );

        // Check if the product exists
        if (deleteProduct == null) {
            return null;
        }

        // Perform the DELETE request to delete the product
        restTemplate.delete(
                "https://fakestoreapi.com/products/" + ProductId
        );

        // Convert and return the deleted product
        return deleteProduct.toProduct();
    }

    @Override
    public List<Category> getAllCategory() {
        // Fetch the list of category titles from the API
        String[] categoryTitles = restTemplate.getForObject(
                "https://fakestoreapi.com/products/categories",
                String[].class
        );

        // Check if the response is not null to avoid NullPointerException
        if (categoryTitles == null) {
            return List.of();
        }

        // Convert the array of category titles to a list of Category objects
        return Arrays.stream(categoryTitles)
                .map(title -> {
                    Category category = new Category();
                    category.setTitle(title);
                    return category;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Category getCategoryByTitle(String title) {
        // Since the API does not provide a detailed category by title,
        // we simulate fetching by filtering the list of categories.
        List<Category> categories = getAllCategory();
        return categories.stream()
                .filter(cat -> cat.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }
}
