package com.snakeporium_backend.services.customer.cart;

import com.snakeporium_backend.dto.AddProductInCartDto;
import com.snakeporium_backend.dto.CartItemResponseDto;
import com.snakeporium_backend.dto.CartItemsDto;
import com.snakeporium_backend.dto.OrderDto;
import com.snakeporium_backend.entity.CartItems;
import com.snakeporium_backend.entity.Order;
import com.snakeporium_backend.entity.Product;
import com.snakeporium_backend.entity.User;
import com.snakeporium_backend.enums.OrderStatus;
import com.snakeporium_backend.repository.CartItemRepository;
import com.snakeporium_backend.repository.OrderRepository;
import com.snakeporium_backend.repository.ProductRepository;
import com.snakeporium_backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);

        if (activeOrder == null) {
            logger.warn("No active order found for user {}", addProductInCartDto.getUserId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No active order found for user");
        }

        Optional<CartItems> optionalCartItems = cartItemRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());
        logger.debug("Optional Cart Items: {}", optionalCartItems);

        if (optionalCartItems.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
            logger.debug("Optional Product: {}", optionalProduct);
            Optional<User> optionalUser = userRepository.findById(addProductInCartDto.getUserId());
            logger.debug("Optional User: {}", optionalUser);
            if (optionalProduct.isPresent() && optionalUser.isPresent()) {
                CartItems cart = new CartItems();
                cart.setProduct(optionalProduct.get());
                cart.setPrice(optionalProduct.get().getPrice());
                cart.setQuantity(optionalProduct.get().getQuantity() != null ? optionalProduct.get().getQuantity() : 1);  //
                // Ensure
                // quantity is not null
                cart.setUser(optionalUser.get());
                cart.setOrder(activeOrder);

                CartItems updatedCart = cartItemRepository.save(cart);

                activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cart.getPrice());
                activeOrder.setAmount(activeOrder.getAmount() + cart.getPrice());
                activeOrder.getCartItems().add(cart);

                orderRepository.save(activeOrder);

                // Create CartItemResponseDTO
                CartItemResponseDto responseDTO = new CartItemResponseDto();
                responseDTO.setProduct(cart.getProduct());
                responseDTO.setPrice(cart.getPrice());
                responseDTO.setQuantity(cart.getQuantity());
              responseDTO.setUser(cart.getUser());
//                responseDTO.setOrder(cart.getOrder());


                return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Product Not Found");
            }
        }
    }

    public OrderDto getCardByUserId(Long userId) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);

        List<CartItemsDto> cartItemsDtoList =
                activeOrder.getCartItems().stream().map(CartItems ::getCartDto).collect(Collectors.toList());

        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(activeOrder.getAmount());
        orderDto.setId(activeOrder.getId());
        orderDto.setOrderStatus(activeOrder.getOrderStatus());
        orderDto.setDiscount(activeOrder.getDiscount());
        orderDto.setTotalAmount(activeOrder.getTotalAmount());
        orderDto.setCartItems(cartItemsDtoList);

        return orderDto;
    }
}
