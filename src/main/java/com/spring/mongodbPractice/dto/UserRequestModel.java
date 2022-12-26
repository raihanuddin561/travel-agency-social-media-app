package com.spring.mongodbPractice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestModel {
    private String name;
    private String email;
    private String password;
    private String password2;
}
