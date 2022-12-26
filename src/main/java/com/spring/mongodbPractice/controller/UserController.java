package com.spring.mongodbPractice.controller;

import com.spring.mongodbPractice.collections.User;
import com.spring.mongodbPractice.dto.UserRequestModel;
import com.spring.mongodbPractice.dto.UserResponseModel;
import com.spring.mongodbPractice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    public UserResponseModel registerUser(@RequestBody UserRequestModel userRequestModel) throws Exception {
        return userService.registerUser(userRequestModel);
    }
}
