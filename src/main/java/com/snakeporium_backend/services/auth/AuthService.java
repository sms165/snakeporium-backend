package com.snakeporium_backend.services.auth;

import com.snakeporium_backend.dto.RegisterRequest;
import com.snakeporium_backend.dto.UserDto;
import org.springframework.stereotype.Service;


public interface AuthService {

    UserDto createUser(RegisterRequest registerRequest);

   Boolean hasUserWithEmail(String email);
}
