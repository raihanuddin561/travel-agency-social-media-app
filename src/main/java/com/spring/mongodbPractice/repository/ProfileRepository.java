package com.spring.mongodbPractice.repository;

import com.spring.mongodbPractice.collections.Profile;
import com.spring.mongodbPractice.collections.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends MongoRepository<Profile,String> {
    Profile findByUser(User user);
}
