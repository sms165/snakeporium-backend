package com.snakeporium_backend.entity;

import com.snakeporium_backend.dto.WishlistDto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


    public WishlistDto getWishlistDto() {
        WishlistDto wishlistDto = new WishlistDto();

        wishlistDto.setId(id);
        wishlistDto.setProductId(product.getId());
        wishlistDto.setReturnedImg(product.getDto().getByteImg());
        wishlistDto.setProductName(product.getName());
        wishlistDto.setProductDescription(product.getDescription());
        wishlistDto.setPrice(product.getPrice());
        wishlistDto.setUserId(user.getId());
        wishlistDto.setLatin(product.getLatin());


        return wishlistDto;

    }

}
