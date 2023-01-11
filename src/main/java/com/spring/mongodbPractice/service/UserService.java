package com.spring.mongodbPractice.service;

import com.spring.mongodbPractice.dto.UserProfileResponseModel;
import com.spring.mongodbPractice.dto.UserRequestModel;
import com.spring.mongodbPractice.dto.UserResponseModel;

public interface UserService {
    UserResponseModel registerUser(UserRequestModel userRequestModel) throws Exception;

    UserResponseModel getUserByEmail(String username);

    UserResponseModel getUserById(String id);

    UserProfileResponseModel getProfile(String name) throws Exception;
}
