package com.spring.mongodbPractice.service;

import com.spring.mongodbPractice.collections.Person;

import java.util.List;

public interface PersonService {
    String save(Person person);

    List<Person> getPersons();

    List<Person> getPersonsStartWith(String name);
    public void delete(String id);

    List<Person> getByPersonAge(Integer minAge, Integer maxAge);
}
