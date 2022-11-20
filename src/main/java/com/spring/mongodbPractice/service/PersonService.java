package com.spring.mongodbPractice.service;

import com.spring.mongodbPractice.collections.Person;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonService {
    String save(Person person);

    List<Person> getPersons();

    List<Person> getPersonsStartWith(String name);
    public void delete(String id);

    List<Person> getByPersonAge(Integer minAge, Integer maxAge);

    Page<Person> search(String name, Integer minAge, Integer maxAge, String city, Pageable pageable);

    List<Document> getOldestPerson();

    List<Document> getPopulatedBycity();
}
