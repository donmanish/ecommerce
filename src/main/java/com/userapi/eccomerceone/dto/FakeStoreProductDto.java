package com.userapi.eccomerceone.dto;

import com.userapi.eccomerceone.model.Category;
import com.userapi.eccomerceone.model.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreProductDto {
  private long id;
  private String title;
  private double price;
  private String category;
  private String description;
  private String image;

  public Product toProduct()
  {
    Product p = new Product();
    p.setId(id);
    p.setTitle(title);
    p.setPrice(price);
    p.setDescription(description);
    p.setImageUrl(image);

   //get all category in my way
    Category cat = new Category();
    cat.setTitle(title);
    p.setCategory(cat);
    return p;
  }

  public Category toCategory()
  {
    //get all category in my way
    Category cat = new Category();
    cat.setTitle(title);
    return cat;
  }



}
