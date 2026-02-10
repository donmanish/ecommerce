package com.userapi.eccomerceone.service;
import com.userapi.eccomerceone.exceptions.ProductNotFoundException;
import com.userapi.eccomerceone.model.Category;
import com.userapi.eccomerceone.model.Product;
import java.util.List;


public interface ProductService {
    //this is class where we have to write diffrent  logic function
    //Map<String, Integer> map = new HashMap<>() // TreMap<>();

    //product related method
    Product  getSingleProduct(Long ProductId) throws ProductNotFoundException;
    List<Product> getAllProducts();
    Product createProduct(Product product);
    Product  updateProduct(Long ProductId, Product updateDetails);
    Product  deleteProduct(Long ProductId);

    //category related method
    List<Category> getAllCategory();
    Category getCategoryByTitle(String title);

}
