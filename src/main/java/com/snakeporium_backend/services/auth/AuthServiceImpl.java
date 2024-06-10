package com.snakeporium_backend.services.auth;


import com.snakeporium_backend.dto.RegisterRequest;
import com.snakeporium_backend.dto.UserDto;
import com.snakeporium_backend.entity.Order;
import com.snakeporium_backend.entity.User;
import com.snakeporium_backend.enums.OrderStatus;
import com.snakeporium_backend.repository.OrderRepository;
import com.snakeporium_backend.repository.UserRepository;
import com.snakeporium_backend.enums.UserRole;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl  implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private OrderRepository orderRepository;

    public UserDto createUser(RegisterRequest registerRequest){
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        // Assuming you have a field for username in the RegisterRequest
//      user.setUsername(registerRequest.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(registerRequest.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        User createdUser = userRepository.save(user);

        Order order = new Order();
        order.setAmount(0.0);
        order.setTotalAmount(0.0);
        order.setDiscount(0.0);
        order.setUser(createdUser);
        order.setOrderStatus(OrderStatus.Pending);
        orderRepository.save(order);


        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());
//        userDto.setUsername(createdUser.getUsername());
        userDto.setEmail(createdUser.getEmail());
        userDto.setRole(createdUser.getRole());
        userDto.setFirstName(createdUser.getFirstName());
        userDto.setLastName(createdUser.getLastName());

        return userDto;
    }

    public Boolean hasUserWithEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    @PostConstruct
    public void createAdminAccount(){
        User adminAccount = userRepository.findByRole(UserRole.ADMIN);
        if(null == adminAccount){
            User user = new User();
            user.setEmail("contact@yblackbox.com");
            user.setFirstName("admin");
            user.setLastName("admin");
            // Retrieve the admin password from the environment variable
//            String adminPassword = System.getenv("ADMIN_PASSWORD");
//            if (adminPassword == null || adminPassword.isEmpty()) {
//                throw new IllegalStateException("Admin password is not set in the environment variables");
//            }

//            user.setPassword(bCryptPasswordEncoder.encode(adminPassword));
            user.setPassword(bCryptPasswordEncoder.encode("admin"));
            user.setRole(UserRole.ADMIN);
            userRepository.save(user);

        }
    }

}
