package com.spring.mongodbPractice.dto;

import com.spring.mongodbPractice.collections.*;
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
    private Experience experience;
    private Address address;
    private Education education;
    private Profile profile;
    private Social social;
}
