package com.spring.mongodbPractice.service.impl;

import com.spring.mongodbPractice.collections.User;
import com.spring.mongodbPractice.dto.UserRequestModel;
import com.spring.mongodbPractice.dto.UserResponseModel;
import com.spring.mongodbPractice.repository.UserRepository;
import com.spring.mongodbPractice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserResponseModel registerUser(UserRequestModel userRequestModel) throws Exception {
        if(!userRequestModel.getPassword().equals(userRequestModel.getPassword2()))
            throw new Exception("password does not mactched");
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userRequestModel,User.class);
        User storedUser = userRepository.save(user);
        UserResponseModel userResponseModel = modelMapper.map(storedUser,UserResponseModel.class);
        return userResponseModel;
    }
}
