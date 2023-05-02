package com.spring.mongodbPractice.service;

import com.spring.mongodbPractice.dto.*;

import java.security.Principal;
import java.util.List;

public interface UserService {
    UserResponseModel registerUser(UserRequestModel userRequestModel) throws Exception;

    UserResponseModel getUserByEmail(String username);

    UserResponseModel getUserById(String id);

    UserProfileResponseModel update(UserRequestModel userRequestModel, String id);

    UserProfileResponseModel saveExperience(List<ExperienceRequestModel> experienceRequestModel, String userId);

    /**
     * method for update Experience
     *
     * @param experienceRequestModel
     * @param userId
     * @param expId
     * @param principal
     * @return UserProfileResponseModel
     * @author raihan
     */
    UserProfileResponseModel updateExperience(ExperienceRequestModel experienceRequestModel, String userId, String expId, Principal principal);

    /**
     * method for delete experience
     *
     * @param userId
     * @param expId
     * @param principal
     * @return UserProfileResponseModel
     * @author raihan
     */
    UserProfileResponseModel deleteExperience(String userId, String expId, Principal principal);

    UserProfileResponseModel saveEducation(List<EducationRequestModel> educationRequestModels, String userId, Principal principal);
}
