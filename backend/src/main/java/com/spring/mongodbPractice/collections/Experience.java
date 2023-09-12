package com.spring.mongodbPractice.collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Experience {
    private String id;
    private String title;
    private String company;
    private String location;
    private Date from;
    private Date to;
    private Boolean current;
    private String description;
}
