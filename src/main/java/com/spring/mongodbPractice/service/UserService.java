package com.spring.mongodbPractice.service;

import com.spring.mongodbPractice.dto.ExperienceRequestModel;
import com.spring.mongodbPractice.dto.UserProfileResponseModel;
import com.spring.mongodbPractice.dto.UserRequestModel;
import com.spring.mongodbPractice.dto.UserResponseModel;

import java.util.List;

public interface UserService {
    UserResponseModel registerUser(UserRequestModel userRequestModel) throws Exception;

    UserResponseModel getUserByEmail(String username);

    UserResponseModel getUserById(String id);

    UserProfileResponseModel update(UserRequestModel userRequestModel, String id);

    UserProfileResponseModel saveExperience(List<ExperienceRequestModel> experienceRequestModel, String userId);
}
