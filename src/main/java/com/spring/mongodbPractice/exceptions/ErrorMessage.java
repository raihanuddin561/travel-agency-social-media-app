package com.spring.mongodbPractice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessage {
    private Date timeStamp;
    private String message;
    private String path;
}