package com.spring.mongodbPractice.dto;

import com.spring.mongodbPractice.collections.Education;
import com.spring.mongodbPractice.collections.Experience;
import com.spring.mongodbPractice.collections.Social;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponseModel {
    private String id;
    private String userId;
    private String company;
    private String webSite;
    private String location;
    private List<String> skills;
    private String bio;
    private String githubUsername;
    private List<Experience> experiences;
    private List<Education> educations;
    private Social social;
    private Date date = new Date();

}
