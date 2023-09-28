package com.spring.mongodbPractice.collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "roles")
public class Role {
    @Id
    private String id;
    private String role;
}
