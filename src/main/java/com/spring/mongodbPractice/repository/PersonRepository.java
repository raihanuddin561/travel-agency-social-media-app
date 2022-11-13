package com.spring.mongodbPractice.repository;

import com.spring.mongodbPractice.collections.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends MongoRepository<Person,String> {
    List<Person> findByFirstNameStartsWithIgnoreCase(String name);

    List<Person> findByAgeBetween(Integer minAge, Integer maxAge);
}
