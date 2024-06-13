package com.snakeporium_backend.services.customer.wishlist;

import com.snakeporium_backend.dto.WishlistDto;
import com.snakeporium_backend.entity.Product;
import com.snakeporium_backend.entity.User;
import com.snakeporium_backend.entity.Wishlist;
import com.snakeporium_backend.repository.ProductRepository;
import com.snakeporium_backend.repository.UserRepository;
import com.snakeporium_backend.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistDto addProductToWishlist(WishlistDto wishlistDto) {
        Optional<Product> optionalProduct = productRepository.findById(wishlistDto.getProductId());
        Optional<User> optionalUser = userRepository.findById(wishlistDto.getUserId());

        if (optionalProduct.isPresent() && optionalUser.isPresent()) {
            Product product = optionalProduct.get();
            User user = optionalUser.get();
            Long productId = wishlistDto.getProductId();
            Long userId = wishlistDto.getUserId();

            // Check if the product is already in the user's wishlist
            Optional<Wishlist> existingWishlist = wishlistRepository.findByUserIdAndProductId(userId, productId);
            if (existingWishlist.isPresent()) {
                // Product is already in the wishlist
                return null; // Or throw an exception if you prefer
            }

            // Add product to wishlist
            Wishlist wishlist = new Wishlist();
            wishlist.setProduct(product);
            wishlist.setUser(user);

            return wishlistRepository.save(wishlist).getWishlistDto();
        } else {
            return null;  // Handle case where Product or User is not found
        }
    }



    public List<WishlistDto> getWishlistByUserId(Long userId) {
        return wishlistRepository.findByUserId(userId).stream().map(Wishlist::
                getWishlistDto).collect(Collectors.toList());
    }

}

