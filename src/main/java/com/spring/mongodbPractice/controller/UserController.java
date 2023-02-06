package com.spring.mongodbPractice.controller;

import com.spring.mongodbPractice.collections.User;
import com.spring.mongodbPractice.dto.ExperienceRequestModel;
import com.spring.mongodbPractice.dto.UserProfileResponseModel;
import com.spring.mongodbPractice.dto.UserRequestModel;
import com.spring.mongodbPractice.dto.UserResponseModel;
import com.spring.mongodbPractice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    public UserResponseModel registerUser(@RequestBody UserRequestModel userRequestModel) throws Exception {
        return userService.registerUser(userRequestModel);
    }
    @GetMapping("/{id}")
    public UserResponseModel getUser(@PathVariable String id){
        return userService.getUserById(id);
    }

    @PutMapping("/update/{id}")
    public UserProfileResponseModel userUpdated(@RequestBody UserRequestModel userRequestModel,@PathVariable String id){
        UserProfileResponseModel userProfileResponseModel = userService.update(userRequestModel,id);
        return userProfileResponseModel;
    }

    @PostMapping("/{userId}/profile/experience/save")
    public UserProfileResponseModel saveExperience(@RequestBody List<ExperienceRequestModel> experienceRequestModel, @PathVariable String userId){
        UserProfileResponseModel userProfileResponseModel = userService.saveExperience(experienceRequestModel,userId);
        return userProfileResponseModel;
    }

}
