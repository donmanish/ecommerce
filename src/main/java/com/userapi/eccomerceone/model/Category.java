package com.userapi.eccomerceone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends BaseModel{
    private String title;
    //make lazy or eager when it not working lazy and eager

    //when make lazy but by default eager
    //@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)

    //make the eager Which give in one time sql code with JOIN
    //@OneToMany(mappedBy = "category", fetch = FetchType.EAGER)


    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;
}
