package com.spring.mongodbPractice.repository;

import com.spring.mongodbPractice.collections.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
}
