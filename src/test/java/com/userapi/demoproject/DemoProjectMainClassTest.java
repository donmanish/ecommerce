package com.userapi.demoproject;

import com.userapi.eccomerceone.ProjectMainClass;
import com.userapi.eccomerceone.model.Category;
import com.userapi.eccomerceone.model.Product;
import com.userapi.eccomerceone.repositories.CategoryRepository;
import com.userapi.eccomerceone.repositories.ProductRepository;

import com.userapi.eccomerceone.repositories.Projection.ProductProjection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = ProjectMainClass.class)

public class DemoProjectMainClassTest {
   @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void contextLads(){

    }
    @Test
    void testingHqlQuries(){
        List<Product> products = productRepository.getProductByCategoryId(1L);
        System.out.println(products.get(0));
    }
    @Test
    void testingNativeQuries(){
        List<Product> products = productRepository.getProductWithNativQueryByCategoryId(1L);
        System.out.println(products.get(0));
    }
    @Test
    void testingHqlProjection(){
        List<ProductProjection> projection = productRepository.getProductByCategoryIdProjection(1L);
        System.out.println(projection.get(0).getId());
    }
    @Test
    void fetchCategoryLazy()
    {
        Category category = categoryRepository.findById(52L).get();
        System.out.println(category.getId());
        System.out.println("We are done here");
        //It give lazyexception error when product list was call

        //to get list product we do and do eager commit product------------------------
//        List<Product> currentProducts = category.getProducts();
//        System.out.println(currentProducts.size());
//
//        //It going to execute a new query to fetch list of products
//        System.out.println("Product fetch");
    }

}
