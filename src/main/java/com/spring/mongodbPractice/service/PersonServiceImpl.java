package com.spring.mongodbPractice.service;

import com.spring.mongodbPractice.collections.Person;
import com.spring.mongodbPractice.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService{
    @Autowired
    private PersonRepository personRepository;
    @Override
    public String save(Person person) {

        return personRepository.save(person).getPersonId();
    }

    @Override
    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    @Override
    public List<Person> getPersonsStartWith(String name) {
        return personRepository.findByFirstNameStartsWithIgnoreCase(name);
    }

    @Override
    public void delete(String id) {
        personRepository.deleteById(id);
    }

    @Override
    public List<Person> getByPersonAge(Integer minAge, Integer maxAge) {
       return personRepository.findByAgeBetween(minAge,maxAge);
    }
}
