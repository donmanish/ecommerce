package com.userapi.eccomerceone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor // for case of parameter constructor
@NoArgsConstructor // for case of default or no argument constructor
@Entity
public class Product extends BaseModel {
  //product design
  private String title;
  private String description;
  private double price;
  private String imageUrl;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
    //@ManyToOne
  private Category category;
  private int weight;
  private int height;

  //directly generate toString() Methods for HQL and native Quries
  //just foor print the product list in console
  @Override
  public String toString() {
    return "Product{" +
            "title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", price=" + price +
            ", imageUrl='" + imageUrl + '\'' +
            ", category=" + category +
            '}';
  }
}
