package com.spring.mongodbPractice.service;

import com.spring.mongodbPractice.collections.User;
import com.spring.mongodbPractice.dto.UserRequestModel;
import com.spring.mongodbPractice.dto.UserResponseModel;

public interface UserService {
    UserResponseModel registerUser(UserRequestModel userRequestModel) throws Exception;
}
