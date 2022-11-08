package com.spring.mongodbPractice.controller;

import com.spring.mongodbPractice.collections.Person;
import com.spring.mongodbPractice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonService personService;
    @PostMapping
    public String save(@RequestBody Person person){
        String personId = personService.save(person);
        return personId;
    }
}
