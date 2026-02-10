package com.userapi.eccomerceone.dto;

import com.userapi.eccomerceone.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String title;
    private String description;
    private double price;
    private String imageUrl;
    private String categoryTitle;
    private int weight;
    private int height;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.weight = product.getWeight();
        this.height = product.getHeight();
        if (product.getCategory() != null) {
            this.categoryTitle = product.getCategory().getTitle();
        }
    }

    // Getters and setters (or use Lombok @Getter @Setter)
}
