package com.spring.mongodbPractice.service.impl;

import com.spring.mongodbPractice.collections.*;
import com.spring.mongodbPractice.dto.*;
import com.spring.mongodbPractice.exceptions.ActionNotPermittedException;
import com.spring.mongodbPractice.exceptions.ErrorMessages;
import com.spring.mongodbPractice.repository.RoleRepository;
import com.spring.mongodbPractice.repository.UserRepository;
import com.spring.mongodbPractice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * User service implementation
 *
 * @author raihan
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    /**
     * register user method
     *
     * @param userRequestModel request body for user info
     * @return UserResponseModel
     * @throws Exception can be occurred exception
     */
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
        Role role = roleRepository.findByRole(RoleEnum.ROLE_USER.name()).orElse(null);
        if (role == null) {
            role = roleRepository.save(Role.builder()
                    .role(RoleEnum.ROLE_USER.name())
                    .build());
        }
        user.setRole(Set.of(role));
        User storedUser = userRepository.save(user);
        return modelMapper.map(storedUser, UserResponseModel.class);
    }

    /**
     * get user by email method
     *
     * @param username is for getting user info
     * @return UserResponseModel
     */
    @Override
    public UserResponseModel getUserByEmail(String username) {
        User user = userRepository.findByEmail(username);
        if (user == null) throw new UsernameNotFoundException("User not found by " + username);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserResponseModel.class);
    }

    /**
     * get user by id
     *
     * @param id is for user id
     * @return UserResponseModel
     */
    @Override
    public UserResponseModel getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UsernameNotFoundException("User not found by id: " + id));
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserResponseModel.class);
    }

    @Override
    public UserProfileResponseModel update(UserRequestModel userRequestModel, String id) {
        User user = userRepository.findById(id).get();
        return null;
    }

    /**
     * save user experience
     *
     * @param experienceRequestModel request body for experience
     * @param userId                 specific user id
     * @return UserProfileResponseModel
     */
    @Override
    public UserProfileResponseModel saveExperience(List<ExperienceRequestModel> experienceRequestModel,
                                                   String userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UsernameNotFoundException("User not found by id: " + userId));
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
            List<Experience> currentExperiences = profile.getExperiences();
            if (currentExperiences != null && currentExperiences.isEmpty()) {
                currentExperiences.addAll(experiences);
                profile.setExperiences(currentExperiences);
            } else {
                profile.setExperiences(experiences);
            }
            user.setProfile(profile);
        }
        User savedUser = userRepository.save(user);
        return UserProfileResponseModel.builder()
                .userId(user.getId())
                .experiences(savedUser.getProfile().getExperiences())
                .date(user.getDate())
                .build();
    }

    /**
     * method for update Experience
     *
     * @param experienceRequestModel request body for experience
     * @param userId                 user id
     * @param expId                  experience id
     * @param principal              to check logged-in user info
     * @return UserProfileResponseModel
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
        return UserProfileResponseModel.builder()
                .userId(currentUser.getId())
                .experiences(savedUser.getProfile().getExperiences())
                .date(savedUser.getDate())
                .build();
    }

    /**
     * method for delete experience
     *
     * @param userId    user id
     * @param expId     experience id
     * @param principal to check logged-in user info
     * @return UserProfileResponseModel
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
        return UserProfileResponseModel.builder()
                .userId(currentUser.getId())
                .experiences(savedUser.getProfile().getExperiences())
                .date(savedUser.getDate())
                .build();
    }

    /**
     * method for save education
     *
     * @param educationRequestModels request body for education
     * @param userId                 user id
     * @param principal              to check logged-in user info
     * @return UserProfileResponseModel
     */
    @Override
    public UserProfileResponseModel saveEducation(List<EducationRequestModel> educationRequestModels, String userId, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName());
        if (!currentUser.getId().equals(userId))
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
            if (currentEducation != null && !currentEducation.isEmpty()) {
                currentEducation.addAll(educations);
                profile.setEducations(currentEducation);
            } else {
                profile.setEducations(educations);
            }
            currentUser.setProfile(profile);
            currentUser = userRepository.save(currentUser);
        }
        return UserProfileResponseModel.builder()
                .userId(currentUser.getId())
                .educations(currentUser.getProfile().getEducations())
                .date(currentUser.getDate())
                .build();
    }

    /**
     * method for update education
     *
     * @param educationRequestModel request body for education
     * @param userId                user id
     * @param expId                 experience id
     * @param principal             to check logged-in user info
     * @return UserProfileResponseModel
     */
    @Override
    public UserProfileResponseModel updateEducation(EducationRequestModel educationRequestModel, String userId, String expId, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName());
        if (!userId.equals(currentUser.getId())) {
            throw new ActionNotPermittedException(ErrorMessages.NOT_PERMITTED_TO_UPDATE.getErrorMessage());
        }
        ModelMapper modelMapper = new ModelMapper();
        if (educationRequestModel != null) {
            Profile profile = currentUser.getProfile();
            List<Education> educations = profile.getEducations();
            for (Education education : educations) {
                if (education.getId().equals(expId)) {
                    modelMapper.map(educationRequestModel, education);
                }
            }
            profile.setEducations(educations);
            currentUser.setProfile(profile);
            currentUser = userRepository.save(currentUser);
        }
        return UserProfileResponseModel.builder()
                .userId(currentUser.getId())
                .educations(currentUser.getProfile().getEducations())
                .date(currentUser.getDate())
                .build();
    }

    /**
     * method for delete education
     *
     * @param userId    user id
     * @param eduId     education id
     * @param principal to check logged-in user info
     * @return UserProfileResponseModel
     */
    @Override
    public UserProfileResponseModel deleteEducation(String userId, String eduId, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName());
        if (!userId.equals(currentUser.getId())) {
            throw new ActionNotPermittedException(ErrorMessages.NOT_PERMITTED_TO_DELETE.getErrorMessage());
        }
        Profile profile = currentUser.getProfile();
        List<Education> educations = profile.getEducations();
        List<Education> educationList = new ArrayList<>();
        for (Education education : educations) {
            if (!education.getId().equals(eduId)) {
                educationList.add(education);
            }
        }
        profile.setEducations(educationList);
        currentUser.setProfile(profile);
        User savedUser = userRepository.save(currentUser);
        return UserProfileResponseModel.builder()
                .userId(currentUser.getId())
                .educations(savedUser.getProfile().getEducations())
                .date(savedUser.getDate())
                .build();
    }

    /**
     * method for checking existing user
     *
     * @param email to check existing user
     * @return boolean
     */
    private boolean isUserExist(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) return true;
        return false;
    }

    /**
     * method for load username by email
     *
     * @param username to load user by this name or email
     * @return UserDetails
     * @throws UsernameNotFoundException can be occurred exception
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found for " + username);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRole())
            authorities.add(new SimpleGrantedAuthority(role == null ? null : role.getRole()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), authorities);
    }
}
