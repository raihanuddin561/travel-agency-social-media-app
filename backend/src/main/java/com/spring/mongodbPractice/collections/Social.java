package com.spring.mongodbPractice.collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Social {
    private String youtube;
    private String facebook;
    private String twitter;
    private String linkedIn;
}
