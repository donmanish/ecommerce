package com.userapi.eccomerceone.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass //whenever you created product or category use baseclass with that
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date  createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;
    private boolean isDelete;
}
