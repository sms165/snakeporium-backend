package com.snakeporium_backend.services.customer.cart;

import com.snakeporium_backend.dto.*;
import com.snakeporium_backend.entity.*;
import com.snakeporium_backend.enums.OrderStatus;
import com.snakeporium_backend.exceptions.ValidationException;
import com.snakeporium_backend.repository.*;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @Autowired
    private CouponRepository couponRepository;

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

        if(activeOrder.getCoupon() != null){
                orderDto.setCouponName(activeOrder.getCoupon().getName());
        }

        return orderDto;
    }

    public OrderDto applyCoupon(Long userId, String code) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
        Coupon coupon = couponRepository.findByCode(code).orElseThrow(() -> new ValidationException("Coupon Not Found"));

        if (couponIsExpired(coupon)) {
            throw new ValidationException("Coupon is Expired");
        }

        double discountAmount = ((coupon.getDiscount() / 100.0) * activeOrder.getTotalAmount());

        double netAmount = activeOrder.getTotalAmount() - discountAmount;

        activeOrder.setAmount(netAmount);
        activeOrder.setDiscount(discountAmount);
        activeOrder.setCoupon(coupon);

        orderRepository.save(activeOrder);
       return activeOrder.getOrderDto();

    }

    private boolean couponIsExpired(Coupon coupon) {
        Date currentDate = new Date();
        Date expirationDate = coupon.getExpirationDate();

        return expirationDate != null && currentDate.after(expirationDate);
    }

    public OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
        Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());

        Optional<CartItems> optionalCartItems =
                cartItemRepository.findByProductIdAndOrderIdAndUserId(addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());

        if (optionalProduct.isPresent() && optionalCartItems.isPresent()) {
            CartItems cartItem = optionalCartItems.get();
            Product product = optionalProduct.get();

            activeOrder.setAmount(activeOrder.getAmount() + cartItem.getPrice());
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cartItem.getPrice());

            cartItem.setQuantity(cartItem.getQuantity() + 1);

            if (activeOrder.getCoupon() != null)  {
                double discountAmount = ((activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount());

                double netAmount = activeOrder.getTotalAmount() - discountAmount;

                activeOrder.setAmount(netAmount);
                activeOrder.setDiscount(discountAmount);
            }
            cartItemRepository.save(cartItem);
            orderRepository.save(activeOrder);
            return activeOrder.getOrderDto();
        }
        return null;
    }

    public OrderDto decreaseProductQuantity(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
        Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());

        Optional<CartItems> optionalCartItems =
                cartItemRepository.findByProductIdAndOrderIdAndUserId(addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());

        if (optionalProduct.isPresent() && optionalCartItems.isPresent()) {
            CartItems cartItem = optionalCartItems.get();
            Product product = optionalProduct.get();

            activeOrder.setAmount(activeOrder.getAmount() - cartItem.getPrice());
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() - cartItem.getPrice());

            cartItem.setQuantity(cartItem.getQuantity() - 1);

            if (activeOrder.getCoupon() != null)  {
                double discountAmount = ((activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount());

                double netAmount = activeOrder.getTotalAmount() - discountAmount;

                activeOrder.setAmount(netAmount);
                activeOrder.setDiscount(discountAmount);
            }
            cartItemRepository.save(cartItem);
            orderRepository.save(activeOrder);
            return activeOrder.getOrderDto();
        }
        return null;
    }

    public OrderDto placeOrder(PlaceOrderDto placeOrderDto) {
        Optional<User> optionalUser = userRepository.findById(placeOrderDto.getUserId());

        if (optionalUser.isPresent()) {
            Order activeOrder = orderRepository.findByUserIdAndOrderStatus(placeOrderDto.getUserId(), OrderStatus.Pending);

            if (activeOrder != null) {
                activeOrder.setOrderDescription(placeOrderDto.getOrderDescription());
                activeOrder.setAddress(placeOrderDto.getAddress());
                activeOrder.setDate(new Date());
                activeOrder.setOrderStatus(OrderStatus.Placed);

                // Ensure the tracking ID is unique
                UUID trackingId = UUID.randomUUID();
                while (orderRepository.existsByTrackingId(trackingId)) {
                    trackingId = UUID.randomUUID();
                }
                activeOrder.setTrackingId(trackingId);

                orderRepository.save(activeOrder);

                // Create a new empty order for the user
                Order newOrder = new Order();
                newOrder.setAmount(0.0);
                newOrder.setTotalAmount(0.0);
                newOrder.setDiscount(0.0);
                newOrder.setUser(optionalUser.get());
                newOrder.setOrderStatus(OrderStatus.Pending);
                newOrder.setDate(new Date());

                // Ensure the new order has a unique tracking ID
                UUID newOrderTrackingId = UUID.randomUUID();
                while (orderRepository.existsByTrackingId(newOrderTrackingId)) {
                    newOrderTrackingId = UUID.randomUUID();
                }
                newOrder.setTrackingId(newOrderTrackingId);

                orderRepository.save(newOrder);

                return activeOrder.getOrderDto();
            } else {
                // If there is no active order, create a new one
                Order newOrder = new Order();
                newOrder.setAmount(0.0);
                newOrder.setTotalAmount(0.0);
                newOrder.setDiscount(0.0);
                newOrder.setUser(optionalUser.get());
                newOrder.setOrderStatus(OrderStatus.Pending);
                newOrder.setOrderDescription(placeOrderDto.getOrderDescription());
                newOrder.setAddress(placeOrderDto.getAddress());
                newOrder.setDate(new Date());

                // Ensure the new order has a unique tracking ID
                UUID newOrderTrackingId = UUID.randomUUID();
                while (orderRepository.existsByTrackingId(newOrderTrackingId)) {
                    newOrderTrackingId = UUID.randomUUID();
                }
                newOrder.setTrackingId(newOrderTrackingId);

                orderRepository.save(newOrder);

                return newOrder.getOrderDto();
            }
        }
        return null;
    }

    public List<OrderDto> getMyPlacedOrders(Long userId) {
return orderRepository.findByUserIdAndOrderStatusIn(userId, List.of(OrderStatus.Placed, OrderStatus.Shipped,
        OrderStatus.Delivered)).stream().map(order -> order.getOrderDto()).collect(Collectors.toList());
    }


    public OrderDto searchOrderByTrackingId(UUID trackingId) {
        Optional<Order> optionalOrder = orderRepository.findByTrackingId(trackingId);
        if (optionalOrder.isPresent()) {
            return optionalOrder.get().getOrderDto();
        }
        return null;
    }

}
