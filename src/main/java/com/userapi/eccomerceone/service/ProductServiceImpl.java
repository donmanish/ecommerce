package com.userapi.eccomerceone.service;

import com.userapi.eccomerceone.exceptions.ProductNotFoundException;
import com.userapi.eccomerceone.model.Category;
import com.userapi.eccomerceone.model.Product;
import com.userapi.eccomerceone.repository.CategoryRepository;
import com.userapi.eccomerceone.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("ProductServiceImpl")
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository)
    {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getSingleProduct(Long ProductId) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findById(ProductId);

        if(!product.isPresent())
        {
            throw new ProductNotFoundException("product not found");
        }
        return product.get();

    }

    @Override
    public List<Product> getAllProducts() {
        return  productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {
        String categoryTitle = product.getCategory().getTitle();

        if (categoryTitle == null || categoryTitle.isEmpty()) {
            throw new RuntimeException("Product category title is required");
        }

        Category category = categoryRepository.findByTitle(categoryTitle)
                .orElseGet(() -> {
                    // If not found, create new
                    Category newCat = new Category();
                    newCat.setTitle(categoryTitle);
                    newCat.setDelete(false);
                    Category savedCat = categoryRepository.save(newCat);
                    return savedCat;
                });

        product.setDelete(false);
        product.setCategory(category);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long ProductId, Product updateDetails) throws  ProductNotFoundException{
        Optional<Product> updateProduct = productRepository.findById(ProductId);
        //product present or not----------------------
        if (!updateProduct.isPresent()) {

            // Update other fields as needed
            throw new ProductNotFoundException("product not found");

        }
        Product existingProduct = updateProduct.get();
        existingProduct.setTitle(updateDetails.getTitle());
        existingProduct.setDescription(updateDetails.getDescription());
        return productRepository.save(existingProduct);
    }

    @Override
    public Product deleteProduct(Long ProductId)  throws ProductNotFoundException{
        Optional<Product> deleteProduct = productRepository.findById(ProductId);

        if (!deleteProduct.isPresent()) {
            throw new ProductNotFoundException("product not found");
        }

        Product productToDelete = deleteProduct.get();
        productRepository.delete(productToDelete);
        return productToDelete;
    }

    @Override
    public List<Category> getAllCategory() {
        return  categoryRepository.findAll();
    }

    @Override
    public Category getCategoryByTitle(String title) {
        return categoryRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Category not found with title: " + title));
    }

}

