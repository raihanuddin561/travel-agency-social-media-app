package com.spring.mongodbPractice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationRequestModel {
    private String school;
    private String degree;
    private String fieldOfStudy;
    private Date from;
    private Date to;
}
