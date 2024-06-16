package com.snakeporium_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.snakeporium_backend.dto.ProductDto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String species;

    private String imageFormat;
    private String latin;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] img;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sex_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Sex sex;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Category category;

    public ProductDto getDto() {
        ProductDto dto = new ProductDto();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        dto.setPrice(price);
        dto.setQuantity(quantity);
//        dto.setSpecies(species);
        dto.setSexId(sex.getId());
        dto.setByteImg(img);
        dto.setCategoryId(category.getId());
        dto.setImageFormat(imageFormat);
        dto.setCategoryName(category.getName());
        dto.setSexName(sex.getName());
        dto.setLatin(latin);

        return dto;
    }

}
