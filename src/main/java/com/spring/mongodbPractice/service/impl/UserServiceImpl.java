package com.spring.mongodbPractice.service.impl;

import com.spring.mongodbPractice.collections.Education;
import com.spring.mongodbPractice.collections.Experience;
import com.spring.mongodbPractice.collections.Profile;
import com.spring.mongodbPractice.collections.User;
import com.spring.mongodbPractice.dto.*;
import com.spring.mongodbPractice.exceptions.ActionNotPermittedException;
import com.spring.mongodbPractice.exceptions.ErrorMessages;
import com.spring.mongodbPractice.repository.UserRepository;
import com.spring.mongodbPractice.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;
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
        if (user == null) {
            throw new UsernameNotFoundException("User not found by id: " + id);
        }
        ModelMapper modelMapper = new ModelMapper();
        UserResponseModel userResponseModel = modelMapper.map(user, UserResponseModel.class);
        return userResponseModel;
    }

    @Override
    public UserProfileResponseModel update(UserRequestModel userRequestModel, String id) {
        User user = userRepository.findById(id).get();
        return null;
    }

    @Override
    public UserProfileResponseModel saveExperience(List<ExperienceRequestModel> experienceRequestModel,
                                                   String userId) {
        User user = userRepository.findById(userId).get();
        if (user == null) throw new UsernameNotFoundException("User not found by id: " + userId);
        ModelMapper modelMapper = new ModelMapper();
        if (experienceRequestModel != null) {
            Type listType = new TypeToken<List<Experience>>() {
            }.getType();
            List<Experience> experiences = modelMapper.map(experienceRequestModel, listType);
            for (Experience experience : experiences) {
                experience.setId(UUID.randomUUID().toString());
                if (experience.getCurrent())
                    experience.setCurrent(false);
            }
            Profile profile = user.getProfile();
            if (profile == null) profile = new Profile();
            profile.setExperiences(experiences);
            user.setProfile(profile);
        }
        User savedUser = userRepository.save(user);
        UserProfileResponseModel userProfileResponseModel = UserProfileResponseModel.builder()
                .userId(user.getId())
                .experiences(savedUser.getProfile().getExperiences())
                .date(user.getDate())
                .build();
        return userProfileResponseModel;
    }

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
    @Override
    public UserProfileResponseModel updateExperience(ExperienceRequestModel experienceRequestModel, String userId,
                                                     String expId, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName());
        if (!userId.equals(currentUser.getId())) {
            throw new ActionNotPermittedException(ErrorMessages.NOT_PERMITTED_TO_UPDATE.getErrorMessage());
        }
        User savedUser = null;
        ModelMapper modelMapper = new ModelMapper();
        if (experienceRequestModel != null) {
            Profile profile = currentUser.getProfile();
            List<Experience> experiences = profile.getExperiences();
            for (Experience experience : experiences) {
                if (experience.getId().equals(expId)) {
                    modelMapper.map(experienceRequestModel, experience);
                }
            }
            profile.setExperiences(experiences);
            currentUser.setProfile(profile);
            savedUser = userRepository.save(currentUser);
        }
        UserProfileResponseModel userProfileResponseModel = UserProfileResponseModel.builder()
                .userId(currentUser.getId())
                .experiences(savedUser.getProfile().getExperiences())
                .date(savedUser.getDate())
                .build();
        return userProfileResponseModel;
    }

    /**
     * method for delete experience
     *
     * @param userId
     * @param expId
     * @param principal
     * @return UserProfileResponseModel
     * @author raihan
     */
    @Override
    public UserProfileResponseModel deleteExperience(String userId, String expId, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName());
        if (!userId.equals(currentUser.getId())) {
            throw new ActionNotPermittedException(ErrorMessages.NOT_PERMITTED_TO_DELETE.getErrorMessage());
        }
        Profile profile = currentUser.getProfile();
        List<Experience> experiences = profile.getExperiences();
        List<Experience> experienceList = new ArrayList<>();
        for (Experience experience : experiences) {
            if (!experience.getId().equals(expId)) {
                experienceList.add(experience);
            }
        }
        profile.setExperiences(experienceList);
        currentUser.setProfile(profile);
        User savedUser = userRepository.save(currentUser);
        UserProfileResponseModel userProfileResponseModel = UserProfileResponseModel.builder()
                .userId(currentUser.getId())
                .experiences(savedUser.getProfile().getExperiences())
                .date(savedUser.getDate())
                .build();
        return userProfileResponseModel;
    }

    @Override
    public UserProfileResponseModel saveEducation(List<EducationRequestModel> educationRequestModels, String userId, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName());
        if(!currentUser.getId().equals(userId))
            throw new ActionNotPermittedException(ErrorMessages.NOT_PERMITTED_TO_SAVE.getErrorMessage());
        ModelMapper modelMapper = new ModelMapper();
        if (educationRequestModels != null && !educationRequestModels.isEmpty()) {
            Type listType = new TypeToken<List<Education>>() {
            }.getType();
            List<Education> educations = modelMapper.map(educationRequestModels, listType);
            for (Education experience : educations) {
                experience.setId(UUID.randomUUID().toString());
            }
            Profile profile = currentUser.getProfile();
            if (profile == null) profile = new Profile();
            List<Education> currentEducation = profile.getEducations();
            if(currentEducation!=null && !currentEducation.isEmpty()){
                currentEducation.addAll(educations);
                profile.setEducations(currentEducation);
            }else{
                profile.setEducations(educations);
            }
            currentUser.setProfile(profile);
            currentUser = userRepository.save(currentUser);
        }
        UserProfileResponseModel userProfileResponseModel = UserProfileResponseModel.builder()
                .userId(currentUser.getId())
                .educations(currentUser.getProfile().getEducations())
                .date(currentUser.getDate())
                .build();
        return userProfileResponseModel;
    }

    /**
     * method for checking existing user
     *
     * @param email
     * @return boolean
     * @author raihan
     */
    private boolean isUserExist(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) return true;
        return false;
    }

    /**
     * method for load username by email
     *
     * @param username
     * @return UserDetails
     * @throws UsernameNotFoundException
     * @author raihan
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found for " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
