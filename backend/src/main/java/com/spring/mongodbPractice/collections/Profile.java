package com.spring.mongodbPractice.collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    private String id;
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
