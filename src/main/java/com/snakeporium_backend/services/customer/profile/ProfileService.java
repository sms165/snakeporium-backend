package com.snakeporium_backend.services.customer.profile;

import com.snakeporium_backend.dto.ProfileDto;
import com.snakeporium_backend.dto.UserDto;

public interface ProfileService {

//    UserDto getUserProfile(Long userId);

    UserDto updateProfile(Long userId, UserDto editRequest);

    UserDto getProfileByUserId(Long userId);

    UserDto updatePassword(Long userId, UserDto userDto);
}
