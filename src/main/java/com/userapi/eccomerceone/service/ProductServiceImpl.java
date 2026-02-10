package com.userapi.eccomerceone.service;

import com.userapi.eccomerceone.exceptions.ProductNotFoundException;
import com.userapi.eccomerceone.model.Category;
import com.userapi.eccomerceone.model.Product;
import com.userapi.eccomerceone.repository.CategoryRepository;
import com.userapi.eccomerceone.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("dbStoreProductService")
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
        //a container object which may or may not contain a non-null value
        Optional<Product> p = productRepository.findById(ProductId);
        if(p.isPresent())
        {
            return p.get();
        }
        throw new ProductNotFoundException("product not found");
    }

    @Override
    public List<Product> getAllProducts() {
        return  productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {
        Category cat = categoryRepository.findByTitle(product.getCategory().getTitle());
        if(cat == null)
        {
            Category newcat = new Category();
            newcat.setTitle(product.getCategory().getTitle());
            Category newRow = categoryRepository.save(newcat);
            product.setCategory(newRow);
        } else {
            product.setCategory(cat);
        }

        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    @Override
    public Product updateProduct(Long ProductId, Product updateDetails) {
        Optional<Product> updateProduct = productRepository.findById(ProductId);
        //product present or not----------------------
        if (updateProduct.isPresent()) {
            Product existingProduct = updateProduct.get();
            existingProduct.setTitle(updateDetails.getTitle());
            existingProduct.setDescription(updateDetails.getDescription());
            // Update other fields as needed

            return productRepository.save(existingProduct);
        } else {
            // Handle case where product with given ID is not found
            return null;
        }
    }

    @Override
    public Product deleteProduct(Long ProductId) {
        Optional<Product> deleteProduct = productRepository.findById(ProductId);

        if (deleteProduct.isPresent()) {
            Product productToDelete = deleteProduct.get();
            productRepository.delete(productToDelete);
            return productToDelete;
        } else {
            // Handle case where product with given ID is not found
            return null;
        }
    }

    @Override
    public List<Category> getAllCategory() {
        return  categoryRepository.findAll();
    }

    @Override
    public Category getCategoryByTitle(String title) {
        Optional<Category> c = Optional.ofNullable(categoryRepository.findByTitle(title));
        return c.get();

    }
}

