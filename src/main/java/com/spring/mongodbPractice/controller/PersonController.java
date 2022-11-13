package com.spring.mongodbPractice.controller;

import com.spring.mongodbPractice.collections.Person;
import com.spring.mongodbPractice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping
    public List<Person> getPersonStartWith(@RequestParam("name") String name){
        List<Person> list = personService.getPersonsStartWith(name);
        return list;
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        personService.delete(id);
    }
    @GetMapping("/age")
    public List<Person> getByPersonAge(@RequestParam Integer minAge,@RequestParam Integer maxAge){
       return personService.getByPersonAge(minAge,maxAge);
    }
}
