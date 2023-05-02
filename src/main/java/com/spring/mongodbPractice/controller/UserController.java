package com.spring.mongodbPractice.controller;

import com.spring.mongodbPractice.dto.*;
import com.spring.mongodbPractice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * this is user api controller
 *
 * @author raihan
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * api for user registration
     *
     * @param userRequestModel
     * @return
     * @throws Exception
     * @author raihan
     */
    @PostMapping
    public UserResponseModel registerUser(@RequestBody UserRequestModel userRequestModel) throws Exception {
        return userService.registerUser(userRequestModel);
    }

    /**
     * api for getting user info by id
     *
     * @param id
     * @return UserResponseModel
     * @author raihan
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseModel> getUser(@PathVariable String id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    /**
     * api for saving experiences
     *
     * @param experienceRequestModel
     * @param userId
     * @return UserProfileResponseModel
     * @author raihan
     */
    @PostMapping("/{userId}/profile/experience/save")
    public ResponseEntity<UserProfileResponseModel> saveExperience(@RequestBody List<ExperienceRequestModel> experienceRequestModel, @PathVariable String userId) {
        UserProfileResponseModel userProfileResponseModel = userService.saveExperience(experienceRequestModel, userId);
        return new ResponseEntity<>(userProfileResponseModel, HttpStatus.CREATED);
    }

    /**
     * this api is for update specific user experience by userId and exp id
     *
     * @param experienceRequestModel
     * @param userId
     * @param expId
     * @param principal
     * @return UserProfileResponseMode
     * @author raihan
     */
    @PutMapping("/{userId}/profile/experience/{expId}")
    public ResponseEntity<UserProfileResponseModel> updateExperience(@RequestBody ExperienceRequestModel experienceRequestModel, @PathVariable String userId,
                                                                     @PathVariable String expId, Principal principal) {
        UserProfileResponseModel userProfileResponseModel = userService.updateExperience(experienceRequestModel, userId, expId, principal);
        return new ResponseEntity<>(userProfileResponseModel, HttpStatus.OK);
    }

    /**
     * api for deleteExperience
     *
     * @param userId
     * @param expId
     * @param principal
     * @return UserProfileResponseModel
     * @author raihan
     */
    @DeleteMapping("/{userId}/profile/experience/{expId}")
    public ResponseEntity<UserProfileResponseModel> deleteExperience(@PathVariable String userId,
                                                                     @PathVariable String expId, Principal principal) {
        UserProfileResponseModel userProfileResponseModel = userService.deleteExperience(userId, expId, principal);
        return new ResponseEntity<>(userProfileResponseModel, HttpStatus.OK);
    }

    @PostMapping("/{userId}/profile/education/save")
    public ResponseEntity<UserProfileResponseModel> saveEducation(@RequestBody List<EducationRequestModel> educationRequestModels,
                                                                  @PathVariable String userId, Principal principal) {
        UserProfileResponseModel userProfileResponseModel = userService.saveEducation(educationRequestModels, userId,principal);
        return new ResponseEntity<>(userProfileResponseModel, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/profile/education/{expId}")
    public ResponseEntity<UserProfileResponseModel> updateEducation(@RequestBody ExperienceRequestModel experienceRequestModel, @PathVariable String userId,
                                                                     @PathVariable String expId, Principal principal) {
        UserProfileResponseModel userProfileResponseModel = userService.updateExperience(experienceRequestModel, userId, expId, principal);
        return new ResponseEntity<>(userProfileResponseModel, HttpStatus.OK);
    }
    @DeleteMapping("/{userId}/profile/education/{expId}")
    public ResponseEntity<UserProfileResponseModel> deleteEducation(@PathVariable String userId,
                                                                     @PathVariable String expId, Principal principal) {
        UserProfileResponseModel userProfileResponseModel = userService.deleteExperience(userId, expId, principal);
        return new ResponseEntity<>(userProfileResponseModel, HttpStatus.OK);
    }
}
