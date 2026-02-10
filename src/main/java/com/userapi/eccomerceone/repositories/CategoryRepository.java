package com.userapi.eccomerceone.repositories;

import com.userapi.eccomerceone.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  CategoryRepository extends JpaRepository<Category, Long> {
    Category save(Category category);
    Category findByTitle(String title);
}
