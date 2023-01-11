package com.spring.mongodbPractice.service.impl;

import com.spring.mongodbPractice.collections.Profile;
import com.spring.mongodbPractice.collections.User;
import com.spring.mongodbPractice.dto.UserProfileResponseModel;
import com.spring.mongodbPractice.dto.UserRequestModel;
import com.spring.mongodbPractice.dto.UserResponseModel;
import com.spring.mongodbPractice.repository.ProfileRepository;
import com.spring.mongodbPractice.repository.UserRepository;
import com.spring.mongodbPractice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponseModel registerUser(UserRequestModel userRequestModel) throws Exception {
        if (!userRequestModel.getPassword().equals(userRequestModel.getPassword2()))
            throw new Exception("password does not mactched");
        if (isUserExist(userRequestModel.getEmail())) {
            throw new Exception("User already exist");
        }
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userRequestModel, User.class);
        user.setPassword(passwordEncoder.encode(userRequestModel.getPassword()));
        User storedUser = userRepository.save(user);
        UserResponseModel userResponseModel = modelMapper.map(storedUser, UserResponseModel.class);
        return userResponseModel;
    }

    @Override
    public UserResponseModel getUserByEmail(String username) {
        User user = userRepository.findByEmail(username);
        if (user == null) throw new UsernameNotFoundException("User not found by " + username);
        ModelMapper modelMapper = new ModelMapper();
        UserResponseModel userResponseModel = modelMapper.map(user, UserResponseModel.class);
        return userResponseModel;
    }

    @Override
    public UserResponseModel getUserById(String id) {
        User user = userRepository.findById(id).get();
        if(user==null){
            throw new UsernameNotFoundException("User not found by id: "+id);
        }
        ModelMapper modelMapper =new ModelMapper();
        UserResponseModel userResponseModel= modelMapper.map(user,UserResponseModel.class);
        return userResponseModel;
    }

    @Override
    public UserProfileResponseModel getProfile(String name) throws Exception {
        User user = userRepository.findByEmail(name);
        Profile profile = profileRepository.findByUser(user);
        if(profile==null){
            throw new Exception("Profile not found for you! Please update your profile");
        }
        ModelMapper modelMapper = new ModelMapper();
        UserProfileResponseModel userProfileResponseModel = modelMapper.map(profile,UserProfileResponseModel.class);
        userProfileResponseModel.setUserId(user.getId());
        return null;
    }

    private boolean isUserExist(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) return true;
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found for " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
